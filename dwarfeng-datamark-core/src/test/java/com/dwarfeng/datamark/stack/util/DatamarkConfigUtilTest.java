package com.dwarfeng.datamark.stack.util;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据标记配置工具类测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkConfigUtilTest {

    @Test
    public void validatesResourceUrl() {
        NullPointerException exception = MessageContext.call(
                Locale.ENGLISH,
                () -> assertThrows(NullPointerException.class, () -> DatamarkConfigUtil.checkResourceUrl(null))
        );

        assertEquals("Resource URL must not be null", exception.getMessage());
        assertDoesNotThrow(() -> DatamarkConfigUtil.checkResourceUrl("classpath:test.storage"));
    }

    @Test
    public void validatesResourceCharset() {
        NullPointerException nullException = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> assertThrows(NullPointerException.class, () -> DatamarkConfigUtil.checkResourceCharset(null))
        );
        IllegalArgumentException blankException = MessageContext.call(
                Locale.ENGLISH,
                () -> assertThrows(IllegalArgumentException.class, () -> DatamarkConfigUtil.checkResourceCharset(""))
        );
        IllegalArgumentException invalidException = MessageContext.call(
                Locale.ENGLISH,
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> DatamarkConfigUtil.checkResourceCharset("invalid charset")
                )
        );

        assertEquals("资源字符集不能为 null", nullException.getMessage());
        assertEquals("Resource charset must not be blank", blankException.getMessage());
        assertEquals("Resource charset is invalid", invalidException.getMessage());
        assertDoesNotThrow(() -> DatamarkConfigUtil.checkResourceCharset("UTF-8"));
    }
}
