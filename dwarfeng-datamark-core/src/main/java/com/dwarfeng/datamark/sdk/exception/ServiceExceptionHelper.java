package com.dwarfeng.datamark.sdk.exception;

import com.dwarfeng.datamark.stack.exception.*;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 数据标记模块服务异常帮助类。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public final class ServiceExceptionHelper {

    /**
     * 向指定的映射中添加数据标记模块默认的目标映射。
     *
     * <p>
     * 该方法可以在配置类中快速搭建异常目标映射。映射保存异常代码供应器，调用方在映射异常时解析当前代码快照。
     *
     * @param map 指定的映射，允许为 null。
     * @return 添加了默认目标的映射。
     */
    public static Map<Class<? extends Exception>, Supplier<ServiceException.Code>> putDefaultDestination(
            Map<Class<? extends Exception>, Supplier<ServiceException.Code>> map
    ) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }

        map.put(DatamarkException.class, ServiceExceptionCodeSuppliers.DATAMARK_FAILED);
        map.put(IllegalDatamarkValueException.class, ServiceExceptionCodeSuppliers.ILLEGAL_DATAMARK_VALUE);
        map.put(ResourceNotWritableException.class, ServiceExceptionCodeSuppliers.RESOURCE_NOT_WRITABLE);
        map.put(ResourceReadFailedException.class, ServiceExceptionCodeSuppliers.RESOURCE_READ_FAILED);
        map.put(ResourceWriteFailedException.class, ServiceExceptionCodeSuppliers.RESOURCE_WRITE_FAILED);
        map.put(UpdateNotAllowedException.class, ServiceExceptionCodeSuppliers.UPDATE_NOT_ALLOWED);
        map.put(DatamarkQosException.class, ServiceExceptionCodeSuppliers.DATAMARK_QOS_FAILED);
        map.put(AmbiguousDatamarkHandlerException.class, ServiceExceptionCodeSuppliers.AMBIGUOUS_DATAMARK_HANDLER);
        map.put(NoDatamarkHandlerPresentException.class, ServiceExceptionCodeSuppliers.NO_DATAMARK_HANDLER_PRESENT);
        map.put(DatamarkHandlerNotFoundException.class, ServiceExceptionCodeSuppliers.DATAMARK_HANDLER_NOT_FOUND);
        map.put(ListenerResolverException.class, ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_FAILED);
        map.put(AmbiguousListenerResolverException.class, ServiceExceptionCodeSuppliers.AMBIGUOUS_LISTENER_RESOLVER);
        map.put(NoListenerResolverPresentException.class, ServiceExceptionCodeSuppliers.NO_LISTENER_RESOLVER_PRESENT);
        map.put(ListenerResolverNotFoundException.class, ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_NOT_FOUND);

        return map;
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
