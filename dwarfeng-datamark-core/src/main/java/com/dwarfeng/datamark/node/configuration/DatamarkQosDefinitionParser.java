package com.dwarfeng.datamark.node.configuration;

import com.dwarfeng.datamark.impl.handler.DatamarkQosHandlerImpl;
import com.dwarfeng.datamark.impl.service.DatamarkQosServiceImpl;
import com.dwarfeng.datamark.sdk.util.BeanDefinitionParserUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
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
        String qosHandlerName = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("qos-handler-name")
        );
        String qosServiceName = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("qos-service-name")
        );
        String semRef = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("sem-ref")
        );

        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, qosHandlerName);
        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, qosServiceName);

        BeanDefinitionBuilder datamarkQosHandlerBuilder = BeanDefinitionBuilder.rootBeanDefinition(
                DatamarkQosHandlerImpl.class
        );
        datamarkQosHandlerBuilder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkQosHandlerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkQosHandlerBuilder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(
                qosHandlerName, datamarkQosHandlerBuilder.getBeanDefinition()
        );

        BeanDefinitionBuilder datamarkQosServiceBuilder = BeanDefinitionBuilder.rootBeanDefinition(
                DatamarkQosServiceImpl.class
        );
        datamarkQosServiceBuilder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        ConstructorArgumentValues datamarkQosServiceConstructorArgumentValues = new ConstructorArgumentValues();
        datamarkQosServiceConstructorArgumentValues.addIndexedArgumentValue(
                0, new RuntimeBeanReference(qosHandlerName)
        );
        datamarkQosServiceConstructorArgumentValues.addIndexedArgumentValue(
                1, new RuntimeBeanReference(semRef)
        );
        datamarkQosServiceBuilder.getRawBeanDefinition().setConstructorArgumentValues(
                datamarkQosServiceConstructorArgumentValues
        );
        datamarkQosServiceBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkQosServiceBuilder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(
                qosServiceName, datamarkQosServiceBuilder.getBeanDefinition()
        );

        return null;
    }
}
