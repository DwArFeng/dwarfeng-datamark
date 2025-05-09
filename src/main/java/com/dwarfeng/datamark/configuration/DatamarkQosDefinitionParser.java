package com.dwarfeng.datamark.configuration;

import com.dwarfeng.datamark.handler.DatamarkQosHandlerImpl;
import com.dwarfeng.datamark.service.DatamarkQosServiceImpl;
import com.dwarfeng.datamark.util.DefinitionParserUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

/**
 * 数据标记 QoS 相关的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkQosDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, @Nonnull ParserContext parserContext) {
        // 解析 handler-name 属性。
        String handlerName = DefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("handler-name")
        );
        // 解析 service-name 属性。
        String serviceName = DefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("service-name")
        );
        // 检查处理器名称是否重复。
        DefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, serviceName);
        // 构造并注册 DatamarkQosHandler 的 BeanDefinition。
        RootBeanDefinition datamarkQosHandlerBeanDefinition = new RootBeanDefinition(DatamarkQosHandlerImpl.class);
        datamarkQosHandlerBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkQosHandlerBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkQosHandlerBeanDefinition.setLazyInit(true);
        parserContext.getRegistry().registerBeanDefinition(handlerName, datamarkQosHandlerBeanDefinition);
        // 构造并注册 DatamarkQosService 的 BeanDefinition。
        RootBeanDefinition datamarkQosServiceBeanDefinition = new RootBeanDefinition(DatamarkQosServiceImpl.class);
        datamarkQosServiceBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkQosServiceBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkQosServiceBeanDefinition.setLazyInit(true);
        ConstructorArgumentValues datamarkQosServiceConstructorArgumentValues = new ConstructorArgumentValues();
        datamarkQosServiceConstructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(handlerName));
        datamarkQosServiceBeanDefinition.setConstructorArgumentValues(datamarkQosServiceConstructorArgumentValues);
        parserContext.getRegistry().registerBeanDefinition(serviceName, datamarkQosServiceBeanDefinition);

        return null;
    }
}
