package com.dwarfeng.datamark.exception;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 数据标记 QoS 异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkQosException extends HandlerException {

    private static final long serialVersionUID = 6080383376317588824L;

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
