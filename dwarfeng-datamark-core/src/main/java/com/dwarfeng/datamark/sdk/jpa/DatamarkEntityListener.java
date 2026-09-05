package com.dwarfeng.datamark.sdk.jpa;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;
import com.dwarfeng.datamark.sdk.util.SystemPropertyConstants;
import com.dwarfeng.datamark.stack.exception.AmbiguousListenerResolverException;
import com.dwarfeng.datamark.stack.exception.ListenerResolverException;
import com.dwarfeng.datamark.stack.exception.ListenerResolverNotFoundException;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.resolve.ListenerResolveInfo;
import com.dwarfeng.datamark.stack.resolve.ListenerResolver;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据标记实体侦听器。
 *
 * <p>
 * 侦听器在工作时，会寻找参数对象对应的类的所有包含 {@link DatamarkField} 注解的所有声明字段（包括私有字段，但不包括父类字段），
 * 并将这些字段的值通过 Bean 方法设置为当前的数据标记。
 *
 * @author DwArFeng
 * @see DatamarkField
 * @since 1.0.0
 */
public class DatamarkEntityListener {

    private static final String DEFAULT_LISTENER_RESOLVER_NAME = "datamarkHandlerResolver";

    private final Map<String, DatamarkHandler> datamarkHandlerMap;
    private final Map<String, ListenerResolver> listenerResolverMap;

    private final Map<Class<?>, EntityInfo> entityFieldInfoMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile ListenerResolver listenerResolver;
    private volatile ListenerResolverException listenerResolverException;

    public DatamarkEntityListener(Map<String, DatamarkHandler> datamarkHandlerMap) {
        this(datamarkHandlerMap, null);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public DatamarkEntityListener(
            Map<String, DatamarkHandler> datamarkHandlerMap,
            Map<String, ListenerResolver> listenerResolverMap
    ) {
        this.datamarkHandlerMap = Optional.ofNullable(datamarkHandlerMap).orElse(Collections.emptyMap());
        this.listenerResolverMap = Optional.ofNullable(listenerResolverMap).orElse(Collections.emptyMap());
    }

    @SuppressWarnings("DuplicatedCode")
    @PrePersist
    public void prePersist(Object entity) throws Exception {
        lock.readLock().lock();
        try {
            if (entityFieldInfoMap.containsKey(entity.getClass())) {
                updateDatamarkField(entity, entityFieldInfoMap.get(entity.getClass()));
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            if (entityFieldInfoMap.containsKey(entity.getClass())) {
                updateDatamarkField(entity, entityFieldInfoMap.get(entity.getClass()));
            }
            EntityInfo entityInfo = parseEntityInfo(entity);
            entityFieldInfoMap.put(entity.getClass(), entityInfo);
            updateDatamarkField(entity, entityInfo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @PreUpdate
    public void preUpdate(Object entity) throws Exception {
        lock.readLock().lock();
        try {
            if (entityFieldInfoMap.containsKey(entity.getClass())) {
                updateDatamarkField(entity, entityFieldInfoMap.get(entity.getClass()));
            }
        } finally {
            lock.readLock().unlock();
        }
        lock.writeLock().lock();
        try {
            if (entityFieldInfoMap.containsKey(entity.getClass())) {
                updateDatamarkField(entity, entityFieldInfoMap.get(entity.getClass()));
            }
            EntityInfo entityInfo = parseEntityInfo(entity);
            entityFieldInfoMap.put(entity.getClass(), entityInfo);
            updateDatamarkField(entity, entityInfo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void updateDatamarkField(Object entity, EntityInfo entityInfo) throws Exception {
        BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        for (EntityFieldInfo fieldInfo : entityInfo.fieldInfos()) {
            beanWrapper.setPropertyValue(fieldInfo.fieldName(), fieldInfo.datamarkHandler().get());
        }
    }

    private EntityInfo parseEntityInfo(Object entity) throws Exception {
        // 如果 datamarkHandlerMap 为空映射，直接抛出异常。
        if (datamarkHandlerMap.isEmpty()) {
            throw new IllegalStateException(CoreMessages.message(CoreMessageKey.ENTITY_LISTENER_NO_HANDLER));
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        // 遍历 fields 寻找含有 DatamarkField 注解的字段，解析 entityFieldInfo，并添加到 entityFieldInfos 中。
        final List<EntityFieldInfo> entityFieldInfos = new ArrayList<>();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(DatamarkField.class)) {
                continue;
            }
            entityFieldInfos.add(parseEntityFieldInfo(entity, field));
        }
        // 构造结果并返回。
        return new EntityInfo(entityFieldInfos);
    }

    private EntityFieldInfo parseEntityFieldInfo(Object entity, Field field) throws Exception {
        DatamarkField datamarkField = field.getAnnotation(DatamarkField.class);
        // 在方法调用的时候，已经保证了 datamarkField 不会是 null。
        assert datamarkField != null;
        String declaredHandlerName = datamarkField.handlerName();
        String fieldName = field.getName();
        String handlerName = getListenerResolver().resolve(
                new ListenerResolveInfo(
                        declaredHandlerName, entity.getClass(), fieldName, datamarkHandlerMap.keySet()
                )
        );
        // 解析 datamarkHandler。
        DatamarkHandler datamarkHandler;
        /*
         * 当 handlerName 是空字符串时：
         * 1. 如果只有一个 datamarkHandler，那么选用这个 datamarkHandler。
         * 2. 如果有多个 datamarkHandler，抛出异常。
         */
        if (StringUtils.isEmpty(handlerName)) {
            if (datamarkHandlerMap.size() == 1) {
                datamarkHandler = datamarkHandlerMap.values().stream().findAny().get();
            } else {
                throw new IllegalStateException(CoreMessages.message(
                        CoreMessageKey.ENTITY_LISTENER_AMBIGUOUS_HANDLER,
                        entity.getClass().getCanonicalName(), fieldName
                ));
            }
        }
        /*
         * 当 handlerName 不是空字符串时：
         * 1. 取 handlerName 对应的 datamarkHandler。
         * 2. 如果 handlerName 对应的 datamarkHandler 不存在，则抛出异常。
         */
        else {
            if (datamarkHandlerMap.containsKey(handlerName)) {
                datamarkHandler = datamarkHandlerMap.get(handlerName);
            } else {
                throw new IllegalStateException(CoreMessages.message(
                        CoreMessageKey.ENTITY_LISTENER_HANDLER_NOT_FOUND,
                        entity.getClass().getCanonicalName(), fieldName, declaredHandlerName, handlerName
                ));
            }
        }
        // 构造结果并返回。
        return new EntityFieldInfo(datamarkHandler, fieldName);
    }

    private ListenerResolver getListenerResolver() throws ListenerResolverException {
        lock.readLock().lock();
        try {
            if (Objects.nonNull(listenerResolver)) {
                return listenerResolver;
            }
            if (Objects.nonNull(listenerResolverException)) {
                throw listenerResolverException;
            }
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            if (Objects.nonNull(listenerResolver)) {
                return listenerResolver;
            }
            if (Objects.nonNull(listenerResolverException)) {
                throw listenerResolverException;
            }
            try {
                listenerResolver = decideListenerResolver();
                return listenerResolver;
            } catch (ListenerResolverException e) {
                listenerResolverException = e;
                throw e;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private ListenerResolver decideListenerResolver() throws ListenerResolverException {
        String listenerResolverName = System.getProperty(SystemPropertyConstants.LISTENER_RESOLVER_BEAN_NAME);
        /*
         * 当系统属性指定了监听器解析器名称时：
         * 1. 如果应用上下文中存在对应名称的监听器解析器，则使用该解析器。
         * 2. 如果应用上下文中不存在对应名称的监听器解析器，则抛出异常。
         */
        if (StringUtils.isNotEmpty(listenerResolverName)) {
            if (listenerResolverMap.containsKey(listenerResolverName)) {
                return listenerResolverMap.get(listenerResolverName);
            }
            throw new ListenerResolverNotFoundException(listenerResolverName);
        }
        /*
         * 当系统属性未指定监听器解析器名称时：
         * 1. 如果应用上下文中有且仅有一个监听器解析器，则使用该解析器。
         * 2. 如果应用上下文中没有监听器解析器，则使用本征监听器解析器。
         * 3. 如果应用上下文中存在默认名称为 datamarkHandlerResolver 的监听器解析器，则使用该解析器。
         * 4. 如果应用上下文中存在多个监听器解析器且无法决定默认解析器，则抛出异常。
         */
        if (listenerResolverMap.size() == 1) {
            return listenerResolverMap.values().stream().findAny().get();
        }
        if (listenerResolverMap.isEmpty()) {
            return DefaultListenerResolver.INSTANCE;
        }
        if (listenerResolverMap.containsKey(DEFAULT_LISTENER_RESOLVER_NAME)) {
            return listenerResolverMap.get(DEFAULT_LISTENER_RESOLVER_NAME);
        }
        throw new AmbiguousListenerResolverException();
    }

    private record EntityInfo(List<EntityFieldInfo> fieldInfos) {

        private EntityInfo(@NotNull List<EntityFieldInfo> fieldInfos) {
            this.fieldInfos = fieldInfos;
        }

        @Override
        @NotNull
        public List<EntityFieldInfo> fieldInfos() {
            return fieldInfos;
        }

        @Override
        public @NotNull String toString() {
            return "EntityInfo{" +
                    "fieldInfos=" + fieldInfos +
                    '}';
        }
    }

    private record EntityFieldInfo(DatamarkHandler datamarkHandler, String fieldName) {

        private EntityFieldInfo(
                @NotNull DatamarkHandler datamarkHandler,
                @NotNull String fieldName
        ) {
            this.datamarkHandler = datamarkHandler;
            this.fieldName = fieldName;
        }

        @Override
        public @NotNull String toString() {
            return "EntityFieldInfo{" +
                    "datamarkHandler=" + datamarkHandler +
                    ", fieldName='" + fieldName + '\'' +
                    '}';
        }
    }

    private static final class DefaultListenerResolver implements ListenerResolver {

        private static final DefaultListenerResolver INSTANCE = new DefaultListenerResolver();

        @NotNull
        @Override
        public String resolve(@NotNull ListenerResolveInfo info) {
            return StringUtils.defaultString(info.declaredHandlerName());
        }

        @Override
        public String toString() {
            return "DefaultListenerResolver{}";
        }
    }
}
