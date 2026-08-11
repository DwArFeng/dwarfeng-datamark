package com.dwarfeng.datamark.impl.service;

import com.dwarfeng.datamark.stack.exception.DatamarkQosException;
import com.dwarfeng.datamark.stack.handler.DatamarkQosHandler;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据标记 QoS 服务实现测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkQosServiceImplTest {

    @Test
    public void delegatesAllOperations() throws Exception {
        StubQosHandler handler = new StubQosHandler();
        DatamarkQosServiceImpl service = new DatamarkQosServiceImpl(
                handler, exception -> new ServiceException(new ServiceException.Code(1, "mapped"), exception)
        );

        assertEquals(List.of("alpha"), service.listHandlerNames());
        assertTrue(service.updateAllowed("alpha"));
        assertEquals("value", service.get("alpha"));
        assertEquals("value", service.refresh("alpha"));
        assertEquals("updated", service.update("alpha", "updated"));
    }

    @Test
    public void mapsHandlerFailuresToServiceException() {
        DatamarkQosHandler failingHandler = new StubQosHandler() {
            @Override
            public String get(String handlerName) throws HandlerException {
                throw new DatamarkQosException("failed");
            }
        };
        DatamarkQosServiceImpl service = new DatamarkQosServiceImpl(
                failingHandler, exception -> new ServiceException(new ServiceException.Code(100, "mapped"), exception)
        );

        ServiceException exception = assertThrows(ServiceException.class, () -> service.get("alpha"));

        assertEquals(100, exception.getCode().getCode());
        assertEquals("mapped", exception.getCode().getTip());
    }

    private static class StubQosHandler implements DatamarkQosHandler {

        @Override
        public List<String> listHandlerNames() {
            return List.of("alpha");
        }

        @Override
        public boolean updateAllowed(String handlerName) {
            return true;
        }

        @Override
        public String get(String handlerName) throws HandlerException {
            return "value";
        }

        @Override
        public String refresh(String handlerName) {
            return "value";
        }

        @Override
        public String update(String handlerName, String datamarkValue) {
            return datamarkValue;
        }
    }
}
