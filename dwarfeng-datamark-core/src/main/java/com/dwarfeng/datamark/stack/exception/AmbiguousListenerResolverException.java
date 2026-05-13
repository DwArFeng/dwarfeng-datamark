package com.dwarfeng.datamark.stack.exception;

/**
 * 监听器解析器模糊异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class AmbiguousListenerResolverException extends ListenerResolverException {

    private static final long serialVersionUID = -8172710700688166933L;

    public AmbiguousListenerResolverException() {
    }

    public AmbiguousListenerResolverException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "应用上下文中有多个监听器解析器, 但是无法决定默认解析器";
    }
}
