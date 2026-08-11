package com.dwarfeng.datamark.sdk.util;

import com.dwarfeng.datamark.stack.exception.DatamarkException;

import org.jetbrains.annotations.NotNull;

/**
 * 数据标记处理器异常帮助类。
 *
 * @author DwArFeng
 * @since 2.2.0
 */
public final class DatamarkExceptionHelper {

    /**
     * 将指定的异常转化为数据标记处理器异常。
     *
     * @param e 指定的异常。
     * @return 解析后得到的数据标记处理器异常。
     */
    public static DatamarkException parse(@NotNull Exception e) {
        if (e instanceof DatamarkException) {
            return (DatamarkException) e;
        }
        return new DatamarkException(e);
    }

    private DatamarkExceptionHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
