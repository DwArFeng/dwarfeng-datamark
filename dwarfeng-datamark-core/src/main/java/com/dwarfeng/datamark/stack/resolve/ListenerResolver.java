package com.dwarfeng.datamark.stack.resolve;

import com.dwarfeng.datamark.stack.exception.ListenerResolverException;

import org.jetbrains.annotations.NotNull;

/**
 * 监听器解析器。
 *
 * <p>
 * 解析器用于根据实体字段上下文信息，计算目标数据标记处理器名称。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public interface ListenerResolver {

    /**
     * 解析处理器名称。
     *
     * @param info 指定的解析信息。
     * @return 解析得到的处理器名称。
     * @throws ListenerResolverException 解析过程中发生的异常。
     */
    @NotNull
    String resolve(@NotNull ListenerResolveInfo info) throws ListenerResolverException;
}
