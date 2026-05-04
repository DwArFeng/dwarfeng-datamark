package com.dwarfeng.datamark.stack.exception;

/**
 * 资源不可写异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ResourceNotWritableException extends DatamarkException {

    private static final long serialVersionUID = 4529202425581281839L;

    private final String resourceUrl;

    public ResourceNotWritableException(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public ResourceNotWritableException(Throwable cause, String resourceUrl) {
        super(cause);
        this.resourceUrl = resourceUrl;
    }

    @Override
    public String getMessage() {
        return "资源不可写: " + resourceUrl;
    }
}
