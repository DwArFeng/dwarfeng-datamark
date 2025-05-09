package com.dwarfeng.datamark.exception;

/**
 * 非法的数据标记值异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class IllegalDatamarkValueException extends DatamarkException {

    private static final long serialVersionUID = 4556935689104072404L;

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
        return "非法的数据标记值: " + illegalDatamark;
    }
}
