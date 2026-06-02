package com.dwarfeng.datamark.sdk.util;

/**
 * 常量类。
 *
 * @author DwArFeng
 * @since 2.2.0
 */
public final class Constants {

    // region XSD 默认值

    public static final String XSD_DEFAULT_DATAMARK_HANDLER_NAME = "datamarkHandler";
    public static final String XSD_DEFAULT_DATAMARK_QOS_HANDLER_NAME = "datamarkQosHandler";
    public static final String XSD_DEFAULT_DATAMARK_QOS_SERVICE_NAME = "datamarkQosService";
    public static final String XSD_DEFAULT_SERVICE_EXCEPTION_MAPPER_NAME = "mapServiceExceptionMapper";

    // endregion

    private Constants() {
        throw new IllegalStateException("禁止实例化");
    }
}
