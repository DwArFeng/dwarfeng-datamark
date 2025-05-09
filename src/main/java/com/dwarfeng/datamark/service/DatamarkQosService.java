package com.dwarfeng.datamark.service;

import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.service.Service;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 数据标记 QoS 服务。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public interface DatamarkQosService extends Service {

    /**
     * 列出所有处理器的名称。
     *
     * @return 所有处理器的名称组成的列表。
     * @throws ServiceException 服务异常。
     */
    List<String> listHandlerNames() throws ServiceException;

    /**
     * 返回处理器是否允许更新。
     *
     * <p>
     * 参数 <code>handlerName</code> 为对应 {@link com.dwarfeng.datamark.handler.DatamarkHandler}
     * 实例的 <code>bean name</code>。
     *
     * <p>
     * 当应用上下文中只有一个 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 时，
     * 参数 <code>handlerName</code> 可以为 <code>null</code>。
     *
     * @param handlerName 处理器名称。
     * @return 处理器是否允许更新。
     * @throws ServiceException 服务异常。
     */
    boolean updateAllowed(@Nullable String handlerName) throws ServiceException;

    /**
     * 获取数据标记值。
     *
     * <p>
     * 参数 <code>handlerName</code> 为对应 {@link com.dwarfeng.datamark.handler.DatamarkHandler}
     * 实例的 <code>bean name</code>。
     *
     * <p>
     * 当应用上下文中只有一个 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 时，
     * 参数 <code>handlerName</code> 可以为 <code>null</code>。
     *
     * @param handlerName 处理器名称。
     * @return 数据标记值。
     * @throws ServiceException 服务异常。
     */
    String get(@Nullable String handlerName) throws ServiceException;

    /**
     * 刷新数据标记值。
     *
     * <p>
     * 参数 <code>handlerName</code> 为对应 {@link com.dwarfeng.datamark.handler.DatamarkHandler}
     * 实例的 <code>bean name</code>。
     *
     * <p>
     * 当应用上下文中只有一个 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 时，
     * 参数 <code>handlerName</code> 可以为 <code>null</code>。
     *
     * @param handlerName 处理器名称。
     * @return 刷新后的数据标记值。
     * @throws ServiceException 服务异常。
     */
    String refresh(@Nullable String handlerName) throws ServiceException;

    /**
     * 更新数据标记值。
     *
     * <p>
     * 参数 <code>handlerName</code> 为对应 {@link com.dwarfeng.datamark.handler.DatamarkHandler}
     * 实例的 <code>bean name</code>。
     *
     * <p>
     * 当应用上下文中只有一个 {@link com.dwarfeng.datamark.handler.DatamarkHandler} 时，
     * 参数 <code>handlerName</code> 可以为 <code>null</code>。
     *
     * @param handlerName   处理器名称。
     * @param datamarkValue 数据标记值
     * @return 更新后的数据标记值。
     * @throws ServiceException 服务异常。
     */
    String update(@Nullable String handlerName, String datamarkValue) throws ServiceException;
}
