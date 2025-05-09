package com.dwarfeng.datamark.handler;

import com.dwarfeng.subgrade.stack.exception.HandlerException;

/**
 * 数据标记处理器。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface DatamarkHandler {

    /**
     * 返回处理器是否允许更新。
     *
     * @return 处理器是否允许更新。
     * @throws HandlerException 处理器异常。
     */
    boolean updateAllowed() throws HandlerException;

    /**
     * 获取数据标记值。
     *
     * @return 数据标记值。
     * @throws HandlerException 处理器异常。
     */
    String get() throws HandlerException;

    /**
     * 刷新数据标记值。
     *
     * @return 刷新后的数据标记值。
     * @throws HandlerException 处理器异常。
     */
    String refresh() throws HandlerException;

    /**
     * 更新数据标记值。
     *
     * @param datamarkValue 数据标记值。
     * @return 更新后的数据标记值。
     * @throws HandlerException 处理器异常。
     */
    String update(String datamarkValue) throws HandlerException;
}
