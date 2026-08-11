package com.dwarfeng.datamark.impl.resolve;

import com.dwarfeng.datamark.stack.resolve.ListenerResolveInfo;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 本征监听器解析器测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class IntrinsicListenerResolverTest {

    @Test
    public void returnsDeclaredHandlerName() throws Exception {
        ListenerResolveInfo info = new ListenerResolveInfo("handler", Object.class, "field", Set.of("handler"));

        assertEquals("handler", IntrinsicListenerResolver.INSTANCE.resolve(info));
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void exposesSingletonInstance() {
        assertSame(IntrinsicListenerResolver.INSTANCE, IntrinsicListenerResolver.INSTANCE);
    }
}
