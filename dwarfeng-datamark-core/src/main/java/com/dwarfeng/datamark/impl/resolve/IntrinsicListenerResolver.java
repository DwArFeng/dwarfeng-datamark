package com.dwarfeng.datamark.impl.resolve;

import com.dwarfeng.datamark.stack.resolve.ListenerResolveInfo;
import com.dwarfeng.datamark.stack.resolve.ListenerResolver;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * 本征监听器解析器。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class IntrinsicListenerResolver implements ListenerResolver {

    public static final ListenerResolver INSTANCE = new IntrinsicListenerResolver();

    @Nonnull
    @Override
    public String resolve(@Nonnull ListenerResolveInfo info) {
        return StringUtils.defaultString(info.getDeclaredHandlerName());
    }

    @Override
    public String toString() {
        return "IntrinsicListenerResolver{}";
    }
}
