package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 资源写入失败异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ResourceWriteFailedException extends DatamarkException {

    @Serial
    private static final long serialVersionUID = 7886243894180257134L;
    
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
        return CoreMessages.message(CoreMessageKey.EXCEPTION_RESOURCE_WRITE_FAILED, resourceUrl);
    }
}
