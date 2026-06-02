package com.dwarfeng.datamark.sdk.util;

import com.dwarfeng.datamark.stack.exception.DatamarkQosException;

import javax.annotation.Nonnull;

/**
 * 数据标记 QoS 处理器异常帮助类。
 *
 * @author DwArFeng
 * @since 2.2.0
 */
public final class DatamarkQosExceptionHelper {

    /**
     * 将指定的异常转化为数据标记 QoS 处理器异常。
     *
     * @param e 指定的异常。
     * @return 解析后得到的数据标记 QoS 处理器异常。
     */
    public static DatamarkQosException parse(@Nonnull Exception e) {
        if (e instanceof DatamarkQosException) {
            return (DatamarkQosException) e;
        }
        return new DatamarkQosException(e);
    }

    private DatamarkQosExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
