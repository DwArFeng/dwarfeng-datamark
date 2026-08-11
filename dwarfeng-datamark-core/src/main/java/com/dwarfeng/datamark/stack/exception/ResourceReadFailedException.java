package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 资源读取失败异常。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public class ResourceReadFailedException extends DatamarkException {

    @Serial
    private static final long serialVersionUID = 3198273518024052467L;
    
    private final String resourceUrl;

    public ResourceReadFailedException(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public ResourceReadFailedException(Throwable cause, String resourceUrl) {
        super(cause);
        this.resourceUrl = resourceUrl;
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_RESOURCE_READ_FAILED, resourceUrl);
    }
}
