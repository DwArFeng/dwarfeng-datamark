package com.dwarfeng.datamark.stack.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;

import java.io.Serial;

/**
 * 监听器解析器模糊异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class AmbiguousListenerResolverException extends ListenerResolverException {

    @Serial
    private static final long serialVersionUID = -8172710700688166933L;

    public AmbiguousListenerResolverException() {
    }

    public AmbiguousListenerResolverException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return CoreMessages.message(CoreMessageKey.EXCEPTION_AMBIGUOUS_LISTENER_RESOLVER);
    }
}
