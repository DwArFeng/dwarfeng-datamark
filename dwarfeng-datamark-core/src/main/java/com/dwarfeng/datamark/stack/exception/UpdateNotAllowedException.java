package com.dwarfeng.datamark.stack.exception;

/**
 * 更新不允许异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class UpdateNotAllowedException extends DatamarkException {

    private static final long serialVersionUID = -7237699842091157838L;

    public UpdateNotAllowedException() {
    }

    public UpdateNotAllowedException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return "更新不允许";
    }
}
