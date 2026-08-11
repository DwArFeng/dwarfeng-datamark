package com.dwarfeng.datamark.core.internal.i18n;

import static com.dwarfeng.datamark.core.internal.i18n.CoreMessages.Catalog.*;

/**
 * Core 模块消息键。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public enum CoreMessageKey {

    EXCEPTION_AMBIGUOUS_DATAMARK_HANDLER(STACK, "exception.ambiguous_datamark_handler"),
    EXCEPTION_AMBIGUOUS_LISTENER_RESOLVER(STACK, "exception.ambiguous_listener_resolver"),
    EXCEPTION_DATAMARK_HANDLER_NOT_FOUND(STACK, "exception.datamark_handler_not_found"),
    EXCEPTION_ILLEGAL_DATAMARK_VALUE(STACK, "exception.illegal_datamark_value"),
    EXCEPTION_LISTENER_RESOLVER_NOT_FOUND(STACK, "exception.listener_resolver_not_found"),
    EXCEPTION_NO_DATAMARK_HANDLER_PRESENT(STACK, "exception.no_datamark_handler_present"),
    EXCEPTION_NO_LISTENER_RESOLVER_PRESENT(STACK, "exception.no_listener_resolver_present"),
    EXCEPTION_RESOURCE_NOT_WRITABLE(STACK, "exception.resource_not_writable"),
    EXCEPTION_RESOURCE_READ_FAILED(STACK, "exception.resource_read_failed"),
    EXCEPTION_RESOURCE_WRITE_FAILED(STACK, "exception.resource_write_failed"),
    EXCEPTION_UPDATE_NOT_ALLOWED(STACK, "exception.update_not_allowed"),
    CONFIG_RESOURCE_URL_NULL(STACK, "config.resource_url.null"),
    CONFIG_RESOURCE_CHARSET_NULL(STACK, "config.resource_charset.null"),
    CONFIG_RESOURCE_CHARSET_BLANK(STACK, "config.resource_charset.blank"),
    CONFIG_RESOURCE_CHARSET_INVALID(STACK, "config.resource_charset.invalid"),
    SERVICE_EXCEPTION_DATAMARK_FAILED(SDK, "service_exception.datamark_failed"),
    SERVICE_EXCEPTION_ILLEGAL_DATAMARK_VALUE(SDK, "service_exception.illegal_datamark_value"),
    SERVICE_EXCEPTION_RESOURCE_NOT_WRITABLE(SDK, "service_exception.resource_not_writable"),
    SERVICE_EXCEPTION_RESOURCE_READ_FAILED(SDK, "service_exception.resource_read_failed"),
    SERVICE_EXCEPTION_RESOURCE_WRITE_FAILED(SDK, "service_exception.resource_write_failed"),
    SERVICE_EXCEPTION_UPDATE_NOT_ALLOWED(SDK, "service_exception.update_not_allowed"),
    SERVICE_EXCEPTION_DATAMARK_QOS_FAILED(SDK, "service_exception.datamark_qos_failed"),
    SERVICE_EXCEPTION_AMBIGUOUS_DATAMARK_HANDLER(SDK, "service_exception.ambiguous_datamark_handler"),
    SERVICE_EXCEPTION_NO_DATAMARK_HANDLER_PRESENT(SDK, "service_exception.no_datamark_handler_present"),
    SERVICE_EXCEPTION_DATAMARK_HANDLER_NOT_FOUND(SDK, "service_exception.datamark_handler_not_found"),
    SERVICE_EXCEPTION_LISTENER_RESOLVER_FAILED(SDK, "service_exception.listener_resolver_failed"),
    SERVICE_EXCEPTION_AMBIGUOUS_LISTENER_RESOLVER(SDK, "service_exception.ambiguous_listener_resolver"),
    SERVICE_EXCEPTION_NO_LISTENER_RESOLVER_PRESENT(SDK, "service_exception.no_listener_resolver_present"),
    SERVICE_EXCEPTION_LISTENER_RESOLVER_NOT_FOUND(SDK, "service_exception.listener_resolver_not_found"),
    ENTITY_LISTENER_NO_HANDLER(SDK, "entity_listener.no_handler"),
    ENTITY_LISTENER_AMBIGUOUS_HANDLER(SDK, "entity_listener.ambiguous_handler"),
    ENTITY_LISTENER_HANDLER_NOT_FOUND(SDK, "entity_listener.handler_not_found"),
    BEAN_DEFINITION_DUPLICATED(SDK, "bean_definition.duplicated"),
    HANDLER_REFRESH_CACHE_START(IMPL, "handler.refresh_cache.start"),
    HANDLER_RESOURCE_READ(IMPL, "handler.resource.read"),
    HANDLER_RESOURCE_EMPTY(IMPL, "handler.resource.empty"),
    HANDLER_RESOURCE_NON_EMPTY(IMPL, "handler.resource.non_empty"),
    HANDLER_REFRESH_FAILED(IMPL, "handler.refresh.failed"),
    HANDLER_CACHE_CURRENT(IMPL, "handler.cache.current"),
    HANDLER_TEMP_DATAMARK_VALIDATE(IMPL, "handler.temp_datamark.validate"),
    HANDLER_TEMP_DATAMARK_INVALID(IMPL, "handler.temp_datamark.invalid"),
    HANDLER_CACHE_UPDATE_FROM_TEMP(IMPL, "handler.cache.update_from_temp"),
    HANDLER_WRITE_CACHE_START(IMPL, "handler.write_cache.start"),
    HANDLER_UPDATE_ALLOWED_VALIDATE(IMPL, "handler.update_allowed.validate"),
    HANDLER_DATAMARK_VALIDATE(IMPL, "handler.datamark.validate"),
    HANDLER_DATAMARK_INVALID(IMPL, "handler.datamark.invalid"),
    HANDLER_RESOURCE_WRITABLE_VALIDATE(IMPL, "handler.resource_writable.validate"),
    HANDLER_RESOURCE_NOT_WRITABLE(IMPL, "handler.resource.not_writable"),
    HANDLER_RESOURCE_WRITE(IMPL, "handler.resource.write"),
    HANDLER_WRITE_FAILED(IMPL, "handler.write.failed"),
    HANDLER_CACHE_UPDATE_FROM_DATAMARK(IMPL, "handler.cache.update_from_datamark"),
    SERVICE_LIST_HANDLER_NAMES_FAILED(IMPL, "service.list_handler_names.failed"),
    SERVICE_UPDATE_ALLOWED_FAILED(IMPL, "service.update_allowed.failed"),
    SERVICE_GET_FAILED(IMPL, "service.get.failed"),
    SERVICE_REFRESH_FAILED(IMPL, "service.refresh.failed"),
    SERVICE_UPDATE_FAILED(IMPL, "service.update.failed");

    private final CoreMessages.Catalog catalog;
    private final String key;

    CoreMessageKey(CoreMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    CoreMessages.Catalog catalog() {
        return catalog;
    }

    /**
     * 返回资源键。
     *
     * @return 资源键。
     */
    public String key() {
        return key;
    }
}
