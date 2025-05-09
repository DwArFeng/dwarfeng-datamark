package com.dwarfeng.datamark.util;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * 数据标记值工具类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class DatamarkValueUtil {

    /**
     * 检查数据标记值是否合法。
     *
     * @param datamark 数据标记值。
     * @return 数据标记值是否合法。
     */
    // 为了代码的可阅读性，此处不做简化。
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isDatamarkValueValid(@Nullable String datamark) {
        if (Objects.isNull(datamark)) {
            return false;
        }
        if (datamark.length() > Constraints.LENGTH_DATAMARK_VALUE) {
            return false;
        }
        return datamark.matches("^\\S+(?:[\\S ]*\\S+|)$");
    }

    private DatamarkValueUtil() {
        throw new IllegalStateException("禁止实例化");
    }
}
