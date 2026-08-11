package com.dwarfeng.datamark.api.internal.i18n;

import static com.dwarfeng.datamark.api.internal.i18n.ApiMessages.Catalog.API;

/**
 * API 模块消息键。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public enum ApiMessageKey {

    COMMAND_DESCRIPTION(API, "command.description"),
    COMMAND_HANDLER_NAME_PLACEHOLDER(API, "command.handler_name.placeholder"),
    COMMAND_DATAMARK_VALUE_PLACEHOLDER(API, "command.datamark_value.placeholder"),
    COMMAND_LIST_HANDLERS_OPTION(API, "command.list_handlers.option"),
    COMMAND_UPDATE_ALLOWED_OPTION(API, "command.update_allowed.option"),
    COMMAND_GET_OPTION(API, "command.get.option"),
    COMMAND_REFRESH_OPTION(API, "command.refresh.option"),
    COMMAND_UPDATE_OPTION(API, "command.update.option"),
    COMMAND_HANDLER_NAME_OPTION(API, "command.handler_name.option"),
    COMMAND_DATAMARK_VALUE_OPTION(API, "command.datamark_value.option"),
    COMMAND_INTERNAL_ERROR(API, "command.internal_error"),
    COMMAND_AVAILABLE_HANDLERS(API, "command.available_handlers"),
    COMMAND_EMPTY(API, "command.empty"),
    COMMAND_HANDLER_ITEM(API, "command.handler_item"),
    COMMAND_UPDATE_ALLOWED_RESULT(API, "command.update_allowed.result"),
    COMMAND_GET_RESULT(API, "command.get.result"),
    COMMAND_REFRESH_SUCCESS(API, "command.refresh.success"),
    COMMAND_REFRESH_RESULT(API, "command.refresh.result"),
    COMMAND_UPDATE_SUCCESS(API, "command.update.success"),
    COMMAND_UPDATE_RESULT(API, "command.update.result"),
    COMMAND_HANDLER_NAME_PROMPT(API, "command.handler_name.prompt"),
    COMMAND_DATAMARK_VALUE_PROMPT(API, "command.datamark_value.prompt"),
    COMMAND_DEFAULT_HANDLER(API, "command.default_handler");

    private final ApiMessages.Catalog catalog;
    private final String key;

    ApiMessageKey(ApiMessages.Catalog catalog, String key) {
        this.catalog = catalog;
        this.key = key;
    }

    ApiMessages.Catalog catalog() {
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
