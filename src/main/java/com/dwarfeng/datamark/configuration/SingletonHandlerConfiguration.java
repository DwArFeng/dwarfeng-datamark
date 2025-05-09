package com.dwarfeng.datamark.configuration;

import com.dwarfeng.datamark.handler.DatamarkHandler;
import com.dwarfeng.datamark.handler.DatamarkHandlerImpl;
import com.dwarfeng.datamark.struct.DatamarkConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 单例模式处理器配置。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Configuration
public class SingletonHandlerConfiguration {

    public static final String SPEL_DEFAULT_RESOURCE_URL = "${datamark.resource_url:" +
            "#{T(com.dwarfeng.datamark.struct.DatamarkConfig$Builder).DEFAULT_RESOURCE_URL}}";
    public static final String SPEL_DEFAULT_RESOURCE_CHARSET = "${datamark.resource_charset:" +
            "#{T(com.dwarfeng.datamark.struct.DatamarkConfig$Builder).DEFAULT_RESOURCE_CHARSET}}";
    public static final String SPEL_DEFAULT_UPDATE_ALLOWED = "${datamark.update_allowed:" +
            "#{T(com.dwarfeng.datamark.struct.DatamarkConfig$Builder).DEFAULT_UPDATE_ALLOWED}}";

    private final ApplicationContext ctx;

    // SPEL 太长，故使用常量缩短长度。
    @Value(SPEL_DEFAULT_RESOURCE_URL)
    private String resourceUrl;

    // SPEL 太长，故使用常量缩短长度。
    @Value(SPEL_DEFAULT_RESOURCE_CHARSET)
    private String resourceCharset;

    // SPEL 太长，故使用常量缩短长度。
    @Value(SPEL_DEFAULT_UPDATE_ALLOWED)
    private boolean updateAllowed;

    public SingletonHandlerConfiguration(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean
    public DatamarkHandler datamarkHandler() {
        DatamarkConfig datamarkConfig = new DatamarkConfig(resourceUrl, resourceCharset, updateAllowed);
        return new DatamarkHandlerImpl(ctx, datamarkConfig);
    }
}
