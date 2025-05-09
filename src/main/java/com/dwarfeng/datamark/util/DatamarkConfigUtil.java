package com.dwarfeng.datamark.util;

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
            throw new NullPointerException("资源 URL 不能为 null");
        }
    }

    /**
     * 检查指定的资源字符集是否合法。
     *
     * @param resourceCharset 指定的资源字符集。
     */
    public static void checkResourceCharset(String resourceCharset) {
        if (Objects.isNull(resourceCharset)) {
            throw new NullPointerException("资源字符集不能为 null");
        }
        if (resourceCharset.isEmpty()) {
            throw new IllegalArgumentException("资源字符集不能为空字符串");
        }
        try {
            java.nio.charset.Charset.forName(resourceCharset);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("资源字符集不合法", e);
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
