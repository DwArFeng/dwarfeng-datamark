package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 更新不允许异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class UpdateNotAllowedException extends DatamarkException {

    @Serial
    private static final long serialVersionUID = -7237699842091157838L;

    public UpdateNotAllowedException() {
    }

    public UpdateNotAllowedException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_UPDATE_NOT_ALLOWED);
    }
}
