package com.dwarfeng.datamark.util;

import org.springframework.beans.factory.xml.ParserContext;

/**
 * Parser 工具类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class DefinitionParserUtil {

    /**
     * 确保 bean 名称不重复。
     *
     * @param parserContext Parser 上下文。
     * @param id            Bean 名称。
     * @throws IllegalStateException 如果 bean 名称重复。
     */
    public static void makeSureBeanNameNotDuplicated(ParserContext parserContext, String id) {
        if (parserContext.getRegistry().containsBeanDefinition(id)) {
            throw new IllegalStateException("Duplicated spring bean name: " + id);
        }
    }

    /**
     * 获取可用的 bean 名称。
     *
     * <p>
     * 该方法会首先检查指定的 <code>baseName</code> 是否已经存在，如果不存在，则直接返回该名称。<br>
     * 如果存在，则会在该名称后面添加一个数字后缀，当添加后缀的名称也存在时，后缀的数值逐渐增加，直到找到一个不存在的名称为止。
     *
     * @param parserContext Parser 上下文。
     * @param baseName      基础名称。
     * @return 可用的 bean 名称。
     */
    public static String getAvailableBeanName(ParserContext parserContext, String baseName) {
        if (!parserContext.getRegistry().containsBeanDefinition(baseName)) {
            return baseName;
        }
        String actualName;
        int index = 1;
        do {
            actualName = baseName + (index++);
        } while (parserContext.getRegistry().containsBeanDefinition(actualName));
        return actualName;
    }

    /**
     * 如果指定的属性是一个 placeHolder，则解析它，否则返回原属性。
     *
     * @param parserContext Parser上下文。
     * @param attribute     指定的属性。
     * @return 也许被解析的属性。
     */
    public static String mayResolvePlaceholder(ParserContext parserContext, String attribute) {
        return parserContext.getReaderContext().getEnvironment().resolvePlaceholders(attribute);
    }

    private DefinitionParserUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
