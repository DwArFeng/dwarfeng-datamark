package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 监听器解析器异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ListenerResolverException extends HandlerException {

    private static final long serialVersionUID = 2733600271815542103L;

    public ListenerResolverException() {
    }

    public ListenerResolverException(String message) {
        super(message);
    }

    public ListenerResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerResolverException(Throwable cause) {
        super(cause);
    }
}
