package com.dwarfeng.datamark.exception;

/**
 * 数据标记处理器模糊异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class AmbiguousDatamarkHandlerException extends DatamarkQosException {

    private static final long serialVersionUID = 2349612956708809872L;

    public AmbiguousDatamarkHandlerException() {
    }

    public AmbiguousDatamarkHandlerException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "应用上下文中有多个数据标记处理器, 但是没有指定 handlerName";
    }
}
