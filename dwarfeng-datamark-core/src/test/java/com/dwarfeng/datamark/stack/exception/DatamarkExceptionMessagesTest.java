package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 数据标记稳定异常文本测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkExceptionMessagesTest {

    @Test
    public void shouldResolveAllStableExceptionMessages() {
        for (MessageCase messageCase : messageCases()) {
            Exception exception = messageCase.supplier().get();

            assertEquals(
                    messageCase.english(),
                    MessageContext.call(Locale.ENGLISH, exception::getMessage)
            );
            assertEquals(
                    messageCase.chinese(),
                    MessageContext.call(Locale.SIMPLIFIED_CHINESE, exception::getMessage)
            );
        }
    }

    private List<MessageCase> messageCases() {
        return List.of(
                new MessageCase(
                        AmbiguousDatamarkHandlerException::new,
                        "Multiple datamark handlers exist in the application context, but no handler name was specified",
                        "应用上下文中有多个数据标记处理器, 但是没有指定 handlerName"
                ),
                new MessageCase(
                        AmbiguousListenerResolverException::new,
                        "Multiple listener resolvers exist in the application context, " +
                                "but the default resolver cannot be determined",
                        "应用上下文中有多个监听器解析器, 但是无法决定默认解析器"
                ),
                new MessageCase(
                        () -> new DatamarkHandlerNotFoundException("handler"),
                        "No datamark handler named handler was found in the application context",
                        "应用上下文中没有找到名称为 handler 的数据标记处理器"
                ),
                new MessageCase(
                        () -> new IllegalDatamarkValueException("value"),
                        "Illegal datamark value: value",
                        "非法的数据标记值: value"
                ),
                new MessageCase(
                        () -> new ListenerResolverNotFoundException("resolver"),
                        "No listener resolver named resolver was found in the application context",
                        "应用上下文中没有找到名称为 resolver 的监听器解析器"
                ),
                new MessageCase(
                        NoDatamarkHandlerPresentException::new,
                        "No datamark handler exists in the application context",
                        "应用上下文中没有数据标记处理器"
                ),
                new MessageCase(
                        NoListenerResolverPresentException::new,
                        "No listener resolver exists in the application context",
                        "应用上下文中没有监听器解析器"
                ),
                new MessageCase(
                        () -> new ResourceNotWritableException("resource"),
                        "Resource is not writable: resource",
                        "资源不可写: resource"
                ),
                new MessageCase(
                        () -> new ResourceReadFailedException("resource"),
                        "Failed to read resource: resource",
                        "资源读取失败: resource"
                ),
                new MessageCase(
                        () -> new ResourceWriteFailedException("resource"),
                        "Failed to write resource: resource",
                        "资源写入失败: resource"
                ),
                new MessageCase(
                        UpdateNotAllowedException::new,
                        "Update is not allowed",
                        "更新不允许"
                )
        );
    }

    private record MessageCase(Supplier<? extends Exception> supplier, String english, String chinese) {
    }
}
