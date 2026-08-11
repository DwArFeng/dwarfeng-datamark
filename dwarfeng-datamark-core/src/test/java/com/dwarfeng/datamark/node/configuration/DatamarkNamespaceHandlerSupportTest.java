package com.dwarfeng.datamark.node.configuration;

import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.datamark.stack.struct.DatamarkConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Datamark XML 命名空间测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkNamespaceHandlerSupportTest {

    @Test
    public void loadsMultitonXmlNamespace() {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:com/dwarfeng/datamark/node/spring/multiton/application-context-aop.xml",
                "classpath:com/dwarfeng/datamark/node/spring/multiton/application-context-placeholder.xml",
                "classpath:com/dwarfeng/datamark/node/spring/multiton/application-context-scan.xml",
                "classpath:com/dwarfeng/datamark/node/spring/multiton/application-context-datamark.xml"
        )) {
            assertEquals(3, context.getBeansOfType(DatamarkConfig.class).size());
            assertEquals(3, context.getBeansOfType(DatamarkHandler.class).size());
            assertEquals(1, context.getBeansOfType(DatamarkQosService.class).size());
        }
    }
}
