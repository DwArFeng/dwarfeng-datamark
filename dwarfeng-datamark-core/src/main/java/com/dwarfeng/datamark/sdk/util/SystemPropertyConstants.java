package com.dwarfeng.datamark.sdk.util;

/**
 * 系统属性常量类。
 *
 * <p>
 * 该类中定义了一些系统属性的常量，主要用于在系统中获取系统属性的值。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public final class SystemPropertyConstants {

    /**
     * 监听器使用的解析器名称。
     */
    public static final String LISTENER_RESOLVER_BEAN_NAME = "com.dwarfeng.datamark.listenerResolverBeanName";

    private SystemPropertyConstants() {
        throw new IllegalStateException("禁止实例化");
    }
}
