package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 监听器解析器未找到异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ListenerResolverNotFoundException extends ListenerResolverException {

    @Serial
    private static final long serialVersionUID = 1351851960937893752L;

    private final String listenerResolverName;

    public ListenerResolverNotFoundException(String listenerResolverName) {
        this.listenerResolverName = listenerResolverName;
    }

    public ListenerResolverNotFoundException(Throwable cause, String listenerResolverName) {
        super(cause);
        this.listenerResolverName = listenerResolverName;
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_LISTENER_RESOLVER_NOT_FOUND, listenerResolverName);
    }
}
