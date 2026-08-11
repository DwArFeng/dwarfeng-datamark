package com.dwarfeng.datamark.base.sdk.i18n;

import com.dwarfeng.datamark.base.stack.i18n.MessageCatalog;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 消息解析工具测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class MessagesTest {

    private static final MessageCatalog CATALOG = MessageCatalog.of(
            MessagesTest.class, "com.dwarfeng.datamark.base.sdk.i18n.test-messages"
    );

    @Test
    public void resolvesExplicitLocaleAndFormatsArguments() {
        assertEquals("Hello, Datamark!", Messages.resolve(CATALOG, "greeting", Locale.ENGLISH, "Datamark"));
        assertEquals(
                "你好, Datamark!",
                Messages.resolve(CATALOG, "greeting", Locale.SIMPLIFIED_CHINESE, "Datamark")
        );
    }

    @Test
    public void resolvesCurrentMessageContext() {
        assertEquals(
                "你好, Datamark!",
                MessageContext.call(
                        Locale.SIMPLIFIED_CHINESE,
                        () -> Messages.resolve(CATALOG, "greeting", "Datamark")
                )
        );
    }

    @Test
    public void degradesMissingMessageKey() {
        assertEquals("!missing!", Messages.resolve(CATALOG, "missing", Locale.ENGLISH));
    }
}
