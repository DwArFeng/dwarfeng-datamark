package com.dwarfeng.datamark.stack.exception;

/**
 * 没有数据标记处理器异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class NoDatamarkHandlerPresentException extends DatamarkQosException {

    private static final long serialVersionUID = -5089987951721818337L;

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
