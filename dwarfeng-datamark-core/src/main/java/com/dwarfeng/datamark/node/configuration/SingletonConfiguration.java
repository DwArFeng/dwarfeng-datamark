package com.dwarfeng.datamark.node.configuration;

import com.dwarfeng.datamark.impl.handler.DatamarkHandlerImpl;
import com.dwarfeng.datamark.impl.handler.DatamarkQosHandlerImpl;
import com.dwarfeng.datamark.impl.service.DatamarkQosServiceImpl;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.handler.DatamarkQosHandler;
import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.datamark.stack.struct.DatamarkConfig;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式处理器配置。
 *
 * @author DwArFeng
 * @since 2.2.0
 */
@Configuration
public class SingletonConfiguration {

    /**
     * SPEL: 数据标记资源的 URL。
     */
    public static final String SPEL_DEFAULT_RESOURCE_URL = "${datamark.resource_url:" +
            "#{T(com.dwarfeng.datamark.stack.struct.DatamarkConfig$Builder).DEFAULT_RESOURCE_URL}}";

    /**
     * SPEL: 数据标记资源的字符集。
     */
    public static final String SPEL_DEFAULT_RESOURCE_CHARSET = "${datamark.resource_charset:" +
            "#{T(com.dwarfeng.datamark.stack.struct.DatamarkConfig$Builder).DEFAULT_RESOURCE_CHARSET}}";

    /**
     * SPEL: 是否允许更新数据标记资源。
     */
    public static final String SPEL_DEFAULT_UPDATE_ALLOWED = "${datamark.update_allowed:" +
            "#{T(com.dwarfeng.datamark.stack.struct.DatamarkConfig$Builder).DEFAULT_UPDATE_ALLOWED}}";

    /**
     * 数据标记处理器的 Bean 名称。
     */
    public static final String BEAN_NAME_DATAMARK_HANDLER = "datamarkHandler";

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

    public SingletonConfiguration(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean(name = BEAN_NAME_DATAMARK_HANDLER)
    public DatamarkHandler datamarkHandler() {
        DatamarkConfig datamarkConfig = new DatamarkConfig(resourceUrl, resourceCharset, updateAllowed);
        return new DatamarkHandlerImpl(ctx, datamarkConfig);
    }

    @Bean
    public DatamarkQosHandler datamarkQosHandler() {
        Map<String, DatamarkHandler> datamarkHandlerMap = new HashMap<>();
        datamarkHandlerMap.put(BEAN_NAME_DATAMARK_HANDLER, datamarkHandler());
        return new DatamarkQosHandlerImpl(datamarkHandlerMap);
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    public DatamarkQosService datamarkQosService(ServiceExceptionMapper serviceExceptionMapper) {
        return new DatamarkQosServiceImpl(datamarkQosHandler(), serviceExceptionMapper);
    }
}
