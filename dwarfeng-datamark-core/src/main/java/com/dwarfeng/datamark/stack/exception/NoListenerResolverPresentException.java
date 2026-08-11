package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 没有监听器解析器异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class NoListenerResolverPresentException extends ListenerResolverException {

    @Serial
    private static final long serialVersionUID = -5027197018733321869L;

    public NoListenerResolverPresentException() {
    }

    public NoListenerResolverPresentException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_NO_LISTENER_RESOLVER_PRESENT);
    }
}
