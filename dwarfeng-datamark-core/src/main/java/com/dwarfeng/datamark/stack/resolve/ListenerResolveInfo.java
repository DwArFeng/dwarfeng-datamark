package com.dwarfeng.datamark.stack.resolve;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

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
public record ListenerResolveInfo(String declaredHandlerName, Class<?> entityClass, String fieldName,
                                  Set<String> availableHandlerNames) {

    public ListenerResolveInfo(
            @NotNull String declaredHandlerName,
            @NotNull Class<?> entityClass,
            @NotNull String fieldName,
            @NotNull Set<String> availableHandlerNames
    ) {
        this.declaredHandlerName = declaredHandlerName;
        this.entityClass = entityClass;
        this.fieldName = fieldName;
        this.availableHandlerNames = availableHandlerNames;
    }

    @Override
    @NotNull
    public String declaredHandlerName() {
        return declaredHandlerName;
    }

    @Override
    @NotNull
    public Class<?> entityClass() {
        return entityClass;
    }

    @Override
    @NotNull
    public String fieldName() {
        return fieldName;
    }

    @Override
    @NotNull
    public Set<String> availableHandlerNames() {
        return availableHandlerNames;
    }

    @Override
    public @NonNull String toString() {
        return "ListenerResolveInfo{" +
                "declaredHandlerName='" + declaredHandlerName + '\'' +
                ", entityClass=" + entityClass +
                ", fieldName='" + fieldName + '\'' +
                ", availableHandlerNames=" + availableHandlerNames +
                '}';
    }
}
