package com.dwarfeng.datamark.sdk.exception;

import com.dwarfeng.datamark.core.internal.i18n.CoreMessageKey;
import com.dwarfeng.datamark.core.internal.i18n.CoreMessages;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;

import java.util.function.Supplier;

/**
 * 数据标记模块服务异常代码供应器。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public final class ServiceExceptionCodeSuppliers {

    private static volatile int EXCEPTION_CODE_OFFSET = 20000;

    /**
     * 数据标记操作失败。
     */
    public static final Supplier<ServiceException.Code> DATAMARK_FAILED =
            () -> new ServiceException.Code(
                    offset(0), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_DATAMARK_FAILED)
            );

    /**
     * 数据标记值不合法。
     */
    public static final Supplier<ServiceException.Code> ILLEGAL_DATAMARK_VALUE =
            () -> new ServiceException.Code(
                    offset(1), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_ILLEGAL_DATAMARK_VALUE)
            );

    /**
     * 资源不可写。
     */
    public static final Supplier<ServiceException.Code> RESOURCE_NOT_WRITABLE =
            () -> new ServiceException.Code(
                    offset(2), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_RESOURCE_NOT_WRITABLE)
            );

    /**
     * 资源读取失败。
     */
    public static final Supplier<ServiceException.Code> RESOURCE_READ_FAILED =
            () -> new ServiceException.Code(
                    offset(3), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_RESOURCE_READ_FAILED)
            );

    /**
     * 资源写入失败。
     */
    public static final Supplier<ServiceException.Code> RESOURCE_WRITE_FAILED =
            () -> new ServiceException.Code(
                    offset(4), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_RESOURCE_WRITE_FAILED)
            );

    /**
     * 不允许更新。
     */
    public static final Supplier<ServiceException.Code> UPDATE_NOT_ALLOWED =
            () -> new ServiceException.Code(
                    offset(5), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_UPDATE_NOT_ALLOWED)
            );

    /**
     * 数据标记 QoS 操作失败。
     */
    public static final Supplier<ServiceException.Code> DATAMARK_QOS_FAILED =
            () -> new ServiceException.Code(
                    offset(10), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_DATAMARK_QOS_FAILED)
            );

    /**
     * 数据标记处理器不明确。
     */
    public static final Supplier<ServiceException.Code> AMBIGUOUS_DATAMARK_HANDLER =
            () -> new ServiceException.Code(
                    offset(11), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_AMBIGUOUS_DATAMARK_HANDLER)
            );

    /**
     * 不存在数据标记处理器。
     */
    public static final Supplier<ServiceException.Code> NO_DATAMARK_HANDLER_PRESENT =
            () -> new ServiceException.Code(
                    offset(12), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_NO_DATAMARK_HANDLER_PRESENT)
            );

    /**
     * 未找到数据标记处理器。
     */
    public static final Supplier<ServiceException.Code> DATAMARK_HANDLER_NOT_FOUND =
            () -> new ServiceException.Code(
                    offset(13), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_DATAMARK_HANDLER_NOT_FOUND)
            );

    /**
     * 监听器解析失败。
     */
    public static final Supplier<ServiceException.Code> LISTENER_RESOLVER_FAILED =
            () -> new ServiceException.Code(
                    offset(20), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_LISTENER_RESOLVER_FAILED)
            );

    /**
     * 监听器解析器不明确。
     */
    public static final Supplier<ServiceException.Code> AMBIGUOUS_LISTENER_RESOLVER =
            () -> new ServiceException.Code(
                    offset(21), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_AMBIGUOUS_LISTENER_RESOLVER)
            );

    /**
     * 不存在监听器解析器。
     */
    public static final Supplier<ServiceException.Code> NO_LISTENER_RESOLVER_PRESENT =
            () -> new ServiceException.Code(
                    offset(22), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_NO_LISTENER_RESOLVER_PRESENT)
            );

    /**
     * 未找到监听器解析器。
     */
    public static final Supplier<ServiceException.Code> LISTENER_RESOLVER_NOT_FOUND =
            () -> new ServiceException.Code(
                    offset(23), CoreMessages.message(CoreMessageKey.SERVICE_EXCEPTION_LISTENER_RESOLVER_NOT_FOUND)
            );

    private static int offset(int i) {
        return EXCEPTION_CODE_OFFSET + i;
    }

    /**
     * 获取异常代码的偏移量。
     *
     * @return 异常代码的偏移量。
     */
    public static int getExceptionCodeOffset() {
        return EXCEPTION_CODE_OFFSET;
    }

    /**
     * 设置异常代码的偏移量。
     *
     * <p>
     * 该方法只更新后续生成异常代码所使用的偏移量，已经创建的异常代码保持不变。
     *
     * @param exceptionCodeOffset 指定的异常代码偏移量。
     */
    public static void setExceptionCodeOffset(int exceptionCodeOffset) {
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;
    }

    private ServiceExceptionCodeSuppliers() {
        throw new IllegalStateException("禁止实例化");
    }
}
