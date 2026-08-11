package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 数据标记处理器未找到异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class DatamarkHandlerNotFoundException extends DatamarkQosException {

    @Serial
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
        return CoreMessages.message(CoreMessageKey.EXCEPTION_DATAMARK_HANDLER_NOT_FOUND, handlerName);
    }
}
