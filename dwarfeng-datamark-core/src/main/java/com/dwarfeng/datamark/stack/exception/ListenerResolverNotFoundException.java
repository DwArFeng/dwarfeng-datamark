package com.dwarfeng.datamark.stack.exception;

/**
 * 监听器解析器未找到异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ListenerResolverNotFoundException extends ListenerResolverException {

    private static final long serialVersionUID = 1351851960937893752L;

    private final String listenerResolverName;

    public ListenerResolverNotFoundException(String listenerResolverName) {
        this.listenerResolverName = listenerResolverName;
    }

    public ListenerResolverNotFoundException(Throwable cause, String listenerResolverName) {
        super(cause);
        this.listenerResolverName = listenerResolverName;
    }

    @Override
    public String getMessage() {
        return "应用上下文中没有找到名称为 " + listenerResolverName + " 的监听器解析器";
    }
}
