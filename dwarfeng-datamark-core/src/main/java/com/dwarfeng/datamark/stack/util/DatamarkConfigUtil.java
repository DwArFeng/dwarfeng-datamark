package com.dwarfeng.datamark.stack.util;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.util.Objects;

/**
 * 数据标记配置工具类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class DatamarkConfigUtil {

    /**
     * 检查指定的资源 URL 是否合法。
     *
     * @param resourceUrl 指定的资源 URL。
     */
    public static void checkResourceUrl(String resourceUrl) {
        if (Objects.isNull(resourceUrl)) {
            throw new NullPointerException(CoreMessages.message(CoreMessageKey.CONFIG_RESOURCE_URL_NULL));
        }
    }

    /**
     * 检查指定的资源字符集是否合法。
     *
     * @param resourceCharset 指定的资源字符集。
     */
    public static void checkResourceCharset(String resourceCharset) {
        if (Objects.isNull(resourceCharset)) {
            throw new NullPointerException(CoreMessages.message(CoreMessageKey.CONFIG_RESOURCE_CHARSET_NULL));
        }
        if (resourceCharset.isEmpty()) {
            throw new IllegalArgumentException(CoreMessages.message(CoreMessageKey.CONFIG_RESOURCE_CHARSET_BLANK));
        }
        try {
            java.nio.charset.Charset.forName(resourceCharset);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(CoreMessages.message(CoreMessageKey.CONFIG_RESOURCE_CHARSET_INVALID), e);
        }
    }

    /**
     * 检查更新是否允许。
     *
     * @param updateAllowed 更新是否允许。
     */
    @SuppressWarnings("EmptyMethod")
    public static void checkUpdateAllowed(boolean updateAllowed) {
        // 该参数不需要检查。
    }

    private DatamarkConfigUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
