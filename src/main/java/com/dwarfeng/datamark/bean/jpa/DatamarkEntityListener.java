package com.dwarfeng.datamark.bean.jpa;

import com.dwarfeng.datamark.handler.DatamarkHandler;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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

    private final Map<String, DatamarkHandler> datamarkHandlerMap;

    private final Map<Class<?>, EntityInfo> entityFieldInfoMap = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public DatamarkEntityListener(Map<String, DatamarkHandler> datamarkHandlerMap) {
        this.datamarkHandlerMap = Optional.ofNullable(datamarkHandlerMap).orElse(Collections.emptyMap());
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
        for (EntityFieldInfo fieldInfo : entityInfo.getFieldInfos()) {
            BeanUtilsBean.getInstance().getPropertyUtils().setProperty(
                    entity,
                    fieldInfo.getFieldName(),
                    fieldInfo.getDatamarkHandler().get()
            );
        }
    }

    private EntityInfo parseEntityInfo(Object entity) {
        // 如果 datamarkHandlerMap 为空映射，直接抛出异常。
        if (datamarkHandlerMap.isEmpty()) {
            throw new IllegalStateException("应用上下文中不存在任何 DatamarkHandler");
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

    private EntityFieldInfo parseEntityFieldInfo(Object entity, Field field) {
        DatamarkField datamarkField = field.getAnnotation(DatamarkField.class);
        // 在方法调用的时候，已经保证了 datamarkField 不会是 null。
        assert datamarkField != null;
        String handlerName = datamarkField.handlerName();
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
                String message = entity.getClass().getCanonicalName() + "." + field.getName() +
                        " 字段中 @DatamarkField 注解的 handlerName 未指定（或为空字符串）, " +
                        "但应用上下文中存在多个 DatamarkHandler";
                throw new IllegalStateException(message);
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
                String message = entity.getClass().getCanonicalName() + "." + field.getName() +
                        " 字段中 @DatamarkField 注解的 handlerName 为 " + handlerName +
                        ", 但应用上下文中不存在对应的 DatamarkHandler";
                throw new IllegalStateException(message);
            }
        }
        // 解析 fieldName。
        String fieldName = field.getName();
        // 构造结果并返回。
        return new EntityFieldInfo(datamarkHandler, fieldName);
    }

    private static final class EntityInfo {

        private final List<EntityFieldInfo> fieldInfos;

        public EntityInfo(@Nonnull List<EntityFieldInfo> fieldInfos) {
            this.fieldInfos = fieldInfos;
        }

        @Nonnull
        public List<EntityFieldInfo> getFieldInfos() {
            return fieldInfos;
        }

        @Override
        public String toString() {
            return "EntityInfo{" +
                    "fieldInfos=" + fieldInfos +
                    '}';
        }
    }

    private static final class EntityFieldInfo {

        private final DatamarkHandler datamarkHandler;
        private final String fieldName;

        public EntityFieldInfo(
                @Nonnull DatamarkHandler datamarkHandler,
                @Nonnull String fieldName
        ) {
            this.datamarkHandler = datamarkHandler;
            this.fieldName = fieldName;
        }

        public DatamarkHandler getDatamarkHandler() {
            return datamarkHandler;
        }

        public String getFieldName() {
            return fieldName;
        }

        @Override
        public String toString() {
            return "EntityFieldInfo{" +
                    "datamarkHandler=" + datamarkHandler +
                    ", fieldName='" + fieldName + '\'' +
                    '}';
        }
    }
}
