package com.dwarfeng.datamark.exception;

/**
 * 没有数据标记处理器异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class NoDatamarkHandlerPresentException extends DatamarkQosException {

    private static final long serialVersionUID = 6674749038812520324L;

    public NoDatamarkHandlerPresentException() {
    }

    public NoDatamarkHandlerPresentException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "应用上下文中没有数据标记处理器";
    }
}
