package com.dwarfeng.datamark.configuration;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Datamark 命名空间处理器支持。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkNamespaceHandlerSupport extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("handler", new DatamarkHandlerDefinitionParser());
        registerBeanDefinitionParser("qos", new DatamarkQosDefinitionParser());
    }
}
