package com.dwarfeng.datamark.node.configuration;

import com.dwarfeng.datamark.impl.handler.DatamarkQosHandlerImpl;
import com.dwarfeng.datamark.impl.service.DatamarkQosServiceImpl;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import com.dwarfeng.datamark.stack.handler.DatamarkQosHandler;
import com.dwarfeng.datamark.stack.service.DatamarkQosService;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 单例模式 QoS 配置。
 *
 * @author DwArFeng
 * @since 1.0.0
 */
@Configuration
public class SingletonQosConfiguration {

    private final Map<String, DatamarkHandler> datamarkHandlerMap;
    private final ServiceExceptionMapper sem;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SingletonQosConfiguration(
            Map<String, DatamarkHandler> datamarkHandlerMap,
            ServiceExceptionMapper sem
    ) {
        this.datamarkHandlerMap = Optional.ofNullable(datamarkHandlerMap).orElse(Collections.emptyMap());
        this.sem = sem;
    }

    @Bean
    public DatamarkQosHandler datamarkQosHandler() {
        return new DatamarkQosHandlerImpl(datamarkHandlerMap);
    }

    @Bean
    public DatamarkQosService datamarkQosService() {
        return new DatamarkQosServiceImpl(datamarkQosHandler(), sem);
    }
}
