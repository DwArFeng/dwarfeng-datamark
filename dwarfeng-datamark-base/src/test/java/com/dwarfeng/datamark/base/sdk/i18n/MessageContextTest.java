package com.dwarfeng.datamark.base.sdk.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消息语言环境上下文测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class MessageContextTest {

    @Test
    public void usesDefaultDisplayLocaleWhenUnbound() {
        assertFalse(MessageContext.isBound());
        assertEquals(Locale.getDefault(Locale.Category.DISPLAY), MessageContext.currentLocale());
    }

    @Test
    public void scopesAndRestoresNestedLocales() {
        MessageContext.run(Locale.ENGLISH, () -> {
            assertTrue(MessageContext.isBound());
            assertEquals(Locale.ENGLISH, MessageContext.currentLocale());
            assertEquals(
                    Locale.SIMPLIFIED_CHINESE,
                    MessageContext.call(Locale.SIMPLIFIED_CHINESE, MessageContext::currentLocale)
            );
            assertEquals(Locale.ENGLISH, MessageContext.currentLocale());
        });
        assertFalse(MessageContext.isBound());
    }
}
