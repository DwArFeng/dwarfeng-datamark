package com.dwarfeng.datamark.util;

import com.dwarfeng.datamark.exception.*;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常的帮助工具类。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class ServiceExceptionHelper {

    /**
     * 向指定的映射中添加 dwarfeng-ftp 默认的目标映射。
     * <p>该方法可以在配置类中快速的搭建目标映射。</p>
     *
     * @param map 指定的映射，允许为null。
     * @return 添加了默认目标的映射。
     */
    public static Map<Class<? extends Exception>, ServiceException.Code> putDefaultDestination(
            Map<Class<? extends Exception>, ServiceException.Code> map) {
        if (Objects.isNull(map)) {
            map = new HashMap<>();
        }

        map.put(DatamarkException.class, ServiceExceptionCodes.DATAMARK_FAILED);
        map.put(IllegalDatamarkValueException.class, ServiceExceptionCodes.ILLEGAL_DATAMARK_VALUE);
        map.put(ResourceNotWritableException.class, ServiceExceptionCodes.RESOURCE_NOT_WRITABLE);
        map.put(ResourceReadFailedException.class, ServiceExceptionCodes.RESOURCE_READ_FAILED);
        map.put(ResourceWriteFailedException.class, ServiceExceptionCodes.RESOURCE_WRITE_FAILED);
        map.put(UpdateNotAllowedException.class, ServiceExceptionCodes.UPDATE_NOT_ALLOWED);
        map.put(DatamarkQosException.class, ServiceExceptionCodes.DATAMARK_QOS_FAILED);
        map.put(AmbiguousDatamarkHandlerException.class, ServiceExceptionCodes.AMBIGUOUS_DATAMARK_HANDLER);
        map.put(NoDatamarkHandlerPresentException.class, ServiceExceptionCodes.NO_DATAMARK_HANDLER_PRESENT);
        map.put(DatamarkHandlerNotFoundException.class, ServiceExceptionCodes.DATAMARK_HANDLER_NOT_FOUND);

        return map;
    }

    private ServiceExceptionHelper() {
        throw new IllegalStateException("禁止外部实例化");
    }
}
