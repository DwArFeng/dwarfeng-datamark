package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;

import java.io.Serial;

/**
 * 数据标记 QoS 异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkQosException extends HandlerException {

    @Serial
    private static final long serialVersionUID = 3033589048930668582L;

    public DatamarkQosException() {
    }

    public DatamarkQosException(String message) {
        super(message);
    }

    public DatamarkQosException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatamarkQosException(Throwable cause) {
        super(cause);
    }
}
