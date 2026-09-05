package com.dwarfeng.datamark.node.configuration;

import com.dwarfeng.datamark.impl.handler.DatamarkHandlerImpl;
import com.dwarfeng.datamark.sdk.util.BeanDefinitionParserUtil;
import com.dwarfeng.datamark.stack.struct.DatamarkConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 数据标记处理器相关的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkHandlerDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, @NotNull ParserContext parserContext) {
        /*
         * 阶段 1: 注册 DatamarkConfig 的内部 BeanDefinition。
         */
        RootBeanDefinition datamarkConfigBuilderBeanDefinition = new RootBeanDefinition(DatamarkConfig.Builder.class);
        datamarkConfigBuilderBeanDefinition.getPropertyValues().addPropertyValue(
                "resourceUrl",
                BeanDefinitionParserUtil.mayResolvePlaceholder(
                        parserContext, element.getAttribute("resource-url")
                )
        );
        datamarkConfigBuilderBeanDefinition.getPropertyValues().addPropertyValue(
                "resourceCharset",
                BeanDefinitionParserUtil.mayResolvePlaceholder(
                        parserContext, element.getAttribute("resource-charset")
                )
        );
        datamarkConfigBuilderBeanDefinition.getPropertyValues().addPropertyValue(
                "updateAllowed",
                BeanDefinitionParserUtil.mayResolvePlaceholder(
                        parserContext, element.getAttribute("update-allowed")
                )
        );
        datamarkConfigBuilderBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkConfigBuilderBeanDefinition.setLazyInit(false);
        String datamarkConfigBuilderBeanName = BeanDefinitionParserUtil.parseAvailableBeanName(
                parserContext, "datamarkConfigBuilder"
        );
        parserContext.getRegistry().registerBeanDefinition(
                datamarkConfigBuilderBeanName, datamarkConfigBuilderBeanDefinition
        );

        RootBeanDefinition datamarkConfigBeanDefinition = new RootBeanDefinition(DatamarkConfig.class);
        datamarkConfigBeanDefinition.setFactoryBeanName(datamarkConfigBuilderBeanName);
        datamarkConfigBeanDefinition.setFactoryMethodName("build");
        datamarkConfigBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkConfigBeanDefinition.setLazyInit(false);
        String datamarkConfigBeanName = BeanDefinitionParserUtil.parseAvailableBeanName(
                parserContext, "datamarkConfig"
        );
        parserContext.getRegistry().registerBeanDefinition(datamarkConfigBeanName, datamarkConfigBeanDefinition);

        String handlerName = (String) BeanDefinitionParserUtil.mayResolveSpel(
                parserContext, element.getAttribute("handler-name")
        );

        BeanDefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, handlerName);

        /*
         * 阶段 2: 注册 DatamarkHandler 的 BeanDefinition。
         */
        BeanDefinitionBuilder datamarkHandlerBuilder = BeanDefinitionBuilder.rootBeanDefinition(
                DatamarkHandlerImpl.class
        );
        datamarkHandlerBuilder.getRawBeanDefinition().setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        ConstructorArgumentValues datamarkHandlerConstructorArgumentValues = new ConstructorArgumentValues();
        datamarkHandlerConstructorArgumentValues.addIndexedArgumentValue(
                1, new RuntimeBeanReference(datamarkConfigBeanName)
        );
        datamarkHandlerBuilder.getRawBeanDefinition().setConstructorArgumentValues(
                datamarkHandlerConstructorArgumentValues
        );
        datamarkHandlerBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkHandlerBuilder.setLazyInit(false);
        parserContext.getRegistry().registerBeanDefinition(handlerName, datamarkHandlerBuilder.getBeanDefinition());

        return null;
    }
}
