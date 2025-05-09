package com.dwarfeng.datamark.util;

import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 服务异常代码。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
public final class ServiceExceptionCodes {

    private static int EXCEPTION_CODE_OFFSET = 20000;

    public static final ServiceException.Code DATAMARK_FAILED =
            new ServiceException.Code(offset(0), "datamark failed");
    public static final ServiceException.Code ILLEGAL_DATAMARK_VALUE =
            new ServiceException.Code(offset(1), "illegal datamark value");
    public static final ServiceException.Code RESOURCE_NOT_WRITABLE =
            new ServiceException.Code(offset(2), "resource not writable");
    public static final ServiceException.Code RESOURCE_READ_FAILED =
            new ServiceException.Code(offset(3), "resource read failed");
    public static final ServiceException.Code RESOURCE_WRITE_FAILED =
            new ServiceException.Code(offset(4), "resource write failed");
    public static final ServiceException.Code UPDATE_NOT_ALLOWED =
            new ServiceException.Code(offset(5), "update not allowed");

    public static final ServiceException.Code DATAMARK_QOS_FAILED =
            new ServiceException.Code(offset(10), "datamark qos failed");
    public static final ServiceException.Code AMBIGUOUS_DATAMARK_HANDLER =
            new ServiceException.Code(offset(11), "ambiguous datamark handler");
    public static final ServiceException.Code NO_DATAMARK_HANDLER_PRESENT =
            new ServiceException.Code(offset(12), "no datamark handler present");
    public static final ServiceException.Code DATAMARK_HANDLER_NOT_FOUND =
            new ServiceException.Code(offset(13), "datamark handler not found");

    private static int offset(int i) {
        return EXCEPTION_CODE_OFFSET + i;
    }

    /**
     * 获取异常代号的偏移量。
     *
     * @return 异常代号的偏移量。
     */
    public static int getExceptionCodeOffset() {
        return EXCEPTION_CODE_OFFSET;
    }

    /**
     * 设置异常代号的偏移量。
     *
     * @param exceptionCodeOffset 指定的异常代号的偏移量。
     */
    public static void setExceptionCodeOffset(int exceptionCodeOffset) {
        // 设置 EXCEPTION_CODE_OFFSET 的值。
        EXCEPTION_CODE_OFFSET = exceptionCodeOffset;

        // 以新的 EXCEPTION_CODE_OFFSET 为基准，更新异常代码的值。
        DATAMARK_FAILED.setCode(offset(0));
        ILLEGAL_DATAMARK_VALUE.setCode(offset(1));
        RESOURCE_NOT_WRITABLE.setCode(offset(2));
        RESOURCE_READ_FAILED.setCode(offset(3));
        RESOURCE_WRITE_FAILED.setCode(offset(4));
        UPDATE_NOT_ALLOWED.setCode(offset(5));
        DATAMARK_QOS_FAILED.setCode(offset(10));
        AMBIGUOUS_DATAMARK_HANDLER.setCode(offset(11));
        NO_DATAMARK_HANDLER_PRESENT.setCode(offset(12));
        DATAMARK_HANDLER_NOT_FOUND.setCode(offset(13));
    }

    private ServiceExceptionCodes() {
        throw new IllegalStateException("禁止实例化");
    }
}
