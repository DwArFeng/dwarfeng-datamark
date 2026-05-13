package com.dwarfeng.datamark.stack.exception;

/**
 * 没有监听器解析器异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class NoListenerResolverPresentException extends ListenerResolverException {

    private static final long serialVersionUID = -5027197018733321869L;

    public NoListenerResolverPresentException() {
    }

    public NoListenerResolverPresentException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "应用上下文中没有监听器解析器";
    }
}
