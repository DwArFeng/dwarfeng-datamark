package com.dwarfeng.datamark.util;

/**
 * 约束类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class Constraints {

    /**
     * 数据标记值的长度约束。
     */
    public static final int LENGTH_DATAMARK_VALUE = 100;

    private Constraints() {
        throw new IllegalStateException("禁止实例化");
    }
}
