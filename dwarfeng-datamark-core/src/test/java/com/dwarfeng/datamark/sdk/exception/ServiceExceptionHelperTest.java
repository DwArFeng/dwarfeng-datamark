package com.dwarfeng.datamark.sdk.exception;

import com.dwarfeng.datamark.stack.exception.*;
import com.dwarfeng.subgrade.basic.impl.exception.MapServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据标记模块服务异常帮助类测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class ServiceExceptionHelperTest {

    @Test
    public void shouldProvideCompleteDefaultDestinationForMapper() {
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination =
                ServiceExceptionHelper.putDefaultDestination(null);

        assertEquals(14, destination.size());
        assertSame(ServiceExceptionCodeSuppliers.DATAMARK_FAILED, destination.get(DatamarkException.class));
        assertSame(
                ServiceExceptionCodeSuppliers.ILLEGAL_DATAMARK_VALUE,
                destination.get(IllegalDatamarkValueException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.RESOURCE_NOT_WRITABLE,
                destination.get(ResourceNotWritableException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.RESOURCE_READ_FAILED,
                destination.get(ResourceReadFailedException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.RESOURCE_WRITE_FAILED,
                destination.get(ResourceWriteFailedException.class)
        );
        assertSame(ServiceExceptionCodeSuppliers.UPDATE_NOT_ALLOWED, destination.get(UpdateNotAllowedException.class));
        assertSame(ServiceExceptionCodeSuppliers.DATAMARK_QOS_FAILED, destination.get(DatamarkQosException.class));
        assertSame(
                ServiceExceptionCodeSuppliers.AMBIGUOUS_DATAMARK_HANDLER,
                destination.get(AmbiguousDatamarkHandlerException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.NO_DATAMARK_HANDLER_PRESENT,
                destination.get(NoDatamarkHandlerPresentException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.DATAMARK_HANDLER_NOT_FOUND,
                destination.get(DatamarkHandlerNotFoundException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_FAILED,
                destination.get(ListenerResolverException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.AMBIGUOUS_LISTENER_RESOLVER,
                destination.get(AmbiguousListenerResolverException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.NO_LISTENER_RESOLVER_PRESENT,
                destination.get(NoListenerResolverPresentException.class)
        );
        assertSame(
                ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_NOT_FOUND,
                destination.get(ListenerResolverNotFoundException.class)
        );
    }

    @Test
    public void shouldCreateNewServiceExceptionCodesDuringMapping() {
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination =
                ServiceExceptionHelper.putDefaultDestination(null);
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(
                destination, com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers.UNDEFINED
        );

        ServiceException first = mapper.map(new DatamarkException());
        ServiceException second = mapper.map(new DatamarkException());

        assertEquals(ServiceExceptionCodeSuppliers.getExceptionCodeOffset(), first.getCode().getCode());
        assertEquals(ServiceExceptionCodeSuppliers.getExceptionCodeOffset(), second.getCode().getCode());
        assertNotSame(first.getCode(), second.getCode());
    }
}
