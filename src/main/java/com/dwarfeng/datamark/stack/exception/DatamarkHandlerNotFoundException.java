package com.dwarfeng.datamark.stack.exception;

/**
 * 数据标记处理器未找到异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkHandlerNotFoundException extends DatamarkQosException {

    private static final long serialVersionUID = -9177679671337952547L;

    private final String handlerName;

    public DatamarkHandlerNotFoundException(String handlerName) {
        this.handlerName = handlerName;
    }

    public DatamarkHandlerNotFoundException(Throwable cause, String handlerName) {
        super(cause);
        this.handlerName = handlerName;
    }

    @Override
    public String getMessage() {
        return "应用上下文中没有找到名称为 " + handlerName + " 的数据标记处理器";
    }
}
