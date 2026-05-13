package com.dwarfeng.datamark.stack.resolve;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * 监听器解析信息。
 *
 * <p>
 * 该结构用于描述监听器在解析处理器名称时的上下文信息。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ListenerResolveInfo {

    private final String declaredHandlerName;
    private final Class<?> entityClass;
    private final String fieldName;
    private final Set<String> availableHandlerNames;

    public ListenerResolveInfo(
            @Nonnull String declaredHandlerName,
            @Nonnull Class<?> entityClass,
            @Nonnull String fieldName,
            @Nonnull Set<String> availableHandlerNames
    ) {
        this.declaredHandlerName = declaredHandlerName;
        this.entityClass = entityClass;
        this.fieldName = fieldName;
        this.availableHandlerNames = availableHandlerNames;
    }

    @Nonnull
    public String getDeclaredHandlerName() {
        return declaredHandlerName;
    }

    @Nonnull
    public Class<?> getEntityClass() {
        return entityClass;
    }

    @Nonnull
    public String getFieldName() {
        return fieldName;
    }

    @Nonnull
    public Set<String> getAvailableHandlerNames() {
        return availableHandlerNames;
    }

    @Override
    public String toString() {
        return "ListenerResolveInfo{" +
                "declaredHandlerName='" + declaredHandlerName + '\'' +
                ", entityClass=" + entityClass +
                ", fieldName='" + fieldName + '\'' +
                ", availableHandlerNames=" + availableHandlerNames +
                '}';
    }
}
