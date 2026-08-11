package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 非法的数据标记值异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class IllegalDatamarkValueException extends DatamarkException {

    @Serial
    private static final long serialVersionUID = 4692173951135125763L;
    
    private final String illegalDatamark;

    public IllegalDatamarkValueException(String illegalDatamark) {
        this.illegalDatamark = illegalDatamark;
    }

    public IllegalDatamarkValueException(Throwable cause, String illegalDatamark) {
        super(cause);
        this.illegalDatamark = illegalDatamark;
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_ILLEGAL_DATAMARK_VALUE, illegalDatamark);
    }
}
