package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 没有数据标记处理器异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class NoDatamarkHandlerPresentException extends DatamarkQosException {

    @Serial
    private static final long serialVersionUID = -5089987951721818337L;

    public NoDatamarkHandlerPresentException() {
    }

    public NoDatamarkHandlerPresentException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_NO_DATAMARK_HANDLER_PRESENT);
    }
}
