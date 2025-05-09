package com.dwarfeng.datamark.exception;

/**
 * 资源写入失败异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ResourceWriteFailedException extends DatamarkException {

    private static final long serialVersionUID = 4862659695194720232L;

    private final String resourceUrl;

    public ResourceWriteFailedException(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public ResourceWriteFailedException(Throwable cause, String resourceUrl) {
        super(cause);
        this.resourceUrl = resourceUrl;
    }

    @Override
    public String getMessage() {
        return "资源写入失败: " + resourceUrl;
    }
}
