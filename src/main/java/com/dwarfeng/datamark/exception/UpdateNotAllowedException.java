package com.dwarfeng.datamark.exception;

/**
 * 更新不允许异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class UpdateNotAllowedException extends DatamarkException {

    private static final long serialVersionUID = 3675541404118790603L;

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
