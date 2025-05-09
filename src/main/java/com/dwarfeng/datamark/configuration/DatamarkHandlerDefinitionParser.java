package com.dwarfeng.datamark.configuration;

import com.dwarfeng.datamark.handler.DatamarkHandlerImpl;
import com.dwarfeng.datamark.struct.DatamarkConfig;
import com.dwarfeng.datamark.util.DefinitionParserUtil;
import org.apache.commons.lang3.StringUtils;
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
 * 数据标记处理器相关的 BeanDefinitionParser。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkHandlerDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, @Nonnull ParserContext parserContext) {
        // 解析 handler-name 属性。
        String handlerName = DefinitionParserUtil.mayResolvePlaceholder(
                parserContext, element.getAttribute("handler-name")
        );
        // 检查处理器名称是否重复。
        DefinitionParserUtil.makeSureBeanNameNotDuplicated(parserContext, handlerName);
        // 构造 DatamarkConfig.Builder 的 BeanDefinition。
        RootBeanDefinition datamarkConfigBuilderBeanDefinition = new RootBeanDefinition(DatamarkConfig.Builder.class);
        // 解析 resource-url 属性，并视情况添加到 datamarkConfigBuilderBeanDefinition 的属性值中。
        if (StringUtils.isNotEmpty(element.getAttribute("resource-url"))) {
            datamarkConfigBuilderBeanDefinition.getPropertyValues().add(
                    "resourceUrl",
                    DefinitionParserUtil.mayResolvePlaceholder(
                            parserContext, element.getAttribute("resource-url")
                    )
            );
        }
        // 解析 resource-charset 属性，并视情况添加到 datamarkConfigBuilderBeanDefinition 的属性值中。
        if (StringUtils.isNotEmpty(element.getAttribute("resource-charset"))) {
            datamarkConfigBuilderBeanDefinition.getPropertyValues().add(
                    "resourceCharset",
                    DefinitionParserUtil.mayResolvePlaceholder(
                            parserContext, element.getAttribute("resource-charset")
                    )
            );
        }
        // 解析 update-allowed 属性，并视情况添加到 datamarkConfigBuilderBeanDefinition 的属性值中。
        if (StringUtils.isNotEmpty(element.getAttribute("update-allowed"))) {
            datamarkConfigBuilderBeanDefinition.getPropertyValues().add(
                    "updateAllowed",
                    DefinitionParserUtil.mayResolvePlaceholder(
                            parserContext, element.getAttribute("update-allowed")
                    )
            );
        }
        // 注册 DatamarkConfig.Builder 的 BeanDefinition。
        datamarkConfigBuilderBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkConfigBuilderBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkConfigBuilderBeanDefinition.setLazyInit(false);
        String datamarkConfigBuilderBeanName = DefinitionParserUtil.getAvailableBeanName(
                parserContext, "datamarkConfigBuilder"
        );
        parserContext.getRegistry().registerBeanDefinition(
                datamarkConfigBuilderBeanName, datamarkConfigBuilderBeanDefinition
        );
        // 构造并注册 DatamarkConfig 的 BeanDefinition。
        RootBeanDefinition datamarkConfigBeanDefinition = new RootBeanDefinition(DatamarkConfig.class);
        datamarkConfigBeanDefinition.setFactoryBeanName(datamarkConfigBuilderBeanName);
        datamarkConfigBeanDefinition.setFactoryMethodName("build");
        datamarkConfigBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkConfigBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkConfigBeanDefinition.setLazyInit(false);
        String datamarkConfigBeanName = DefinitionParserUtil.getAvailableBeanName(parserContext, "datamarkConfig");
        parserContext.getRegistry().registerBeanDefinition(datamarkConfigBeanName, datamarkConfigBeanDefinition);
        // 构造并注册 DatamarkHandler 的 BeanDefinition。
        RootBeanDefinition datamarkHandlerBeanDefinition = new RootBeanDefinition(DatamarkHandlerImpl.class);
        datamarkHandlerBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        datamarkHandlerBeanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        datamarkHandlerBeanDefinition.setLazyInit(false);
        ConstructorArgumentValues datamarkHandlerConstructorArgumentValues = new ConstructorArgumentValues();
        datamarkHandlerConstructorArgumentValues.addIndexedArgumentValue(
                1, new RuntimeBeanReference(datamarkConfigBeanName)
        );
        datamarkHandlerBeanDefinition.setConstructorArgumentValues(datamarkHandlerConstructorArgumentValues);
        parserContext.getRegistry().registerBeanDefinition(handlerName, datamarkHandlerBeanDefinition);

        return null;
    }
}
