package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 数据标记异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkException extends HandlerException {

    private static final long serialVersionUID = 6513205825620426393L;

    public DatamarkException() {
    }

    public DatamarkException(String message) {
        super(message);
    }

    public DatamarkException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatamarkException(Throwable cause) {
        super(cause);
    }
}
