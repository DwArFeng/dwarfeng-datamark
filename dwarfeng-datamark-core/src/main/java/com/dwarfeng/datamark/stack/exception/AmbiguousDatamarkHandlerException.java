package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 数据标记处理器模糊异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class AmbiguousDatamarkHandlerException extends DatamarkQosException {

    @Serial
    private static final long serialVersionUID = -9124859902405774433L;

    public AmbiguousDatamarkHandlerException() {
    }

    public AmbiguousDatamarkHandlerException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_AMBIGUOUS_DATAMARK_HANDLER);
    }
}
