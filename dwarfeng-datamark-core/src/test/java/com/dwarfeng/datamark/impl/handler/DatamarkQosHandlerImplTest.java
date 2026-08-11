package com.dwarfeng.datamark.impl.handler;

import com.dwarfeng.datamark.stack.exception.AmbiguousDatamarkHandlerException;
import com.dwarfeng.datamark.stack.exception.DatamarkHandlerNotFoundException;
import com.dwarfeng.datamark.stack.exception.NoDatamarkHandlerPresentException;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据标记 QoS 处理器实现测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkQosHandlerImplTest {

    @Test
    public void listsSortedUnmodifiableHandlerNames() throws Exception {
        Map<String, DatamarkHandler> handlers = new LinkedHashMap<>();
        handlers.put("beta", new StubHandler("beta"));
        handlers.put("alpha", new StubHandler("alpha"));
        DatamarkQosHandlerImpl qosHandler = new DatamarkQosHandlerImpl(handlers);

        List<String> handlerNames = qosHandler.listHandlerNames();

        assertEquals(List.of("alpha", "beta"), handlerNames);
        assertThrows(UnsupportedOperationException.class, () -> handlerNames.add("gamma"));
    }

    @Test
    public void delegatesToSingleOrNamedHandler() throws Exception {
        StubHandler alpha = new StubHandler("alpha");
        StubHandler beta = new StubHandler("beta");

        DatamarkQosHandlerImpl single = new DatamarkQosHandlerImpl(Map.of("alpha", alpha));
        assertEquals("alpha", single.get(null));
        assertTrue(single.updateAllowed(null));

        DatamarkQosHandlerImpl multiple = new DatamarkQosHandlerImpl(Map.of("alpha", alpha, "beta", beta));
        assertEquals("beta", multiple.refresh("beta"));
        assertEquals("updated", multiple.update("alpha", "updated"));
    }

    @Test
    public void rejectsMissingAmbiguousAndUnknownHandlers() {
        DatamarkQosHandlerImpl empty = new DatamarkQosHandlerImpl(null);
        DatamarkQosHandlerImpl multiple = new DatamarkQosHandlerImpl(
                Map.of("alpha", new StubHandler("alpha"), "beta", new StubHandler("beta"))
        );

        assertThrows(NoDatamarkHandlerPresentException.class, () -> empty.get(null));
        assertThrows(AmbiguousDatamarkHandlerException.class, () -> multiple.get(null));
        assertThrows(DatamarkHandlerNotFoundException.class, () -> multiple.get("unknown"));
    }

    private static final class StubHandler implements DatamarkHandler {

        private String value;

        private StubHandler(String value) {
            this.value = value;
        }

        @Override
        public boolean updateAllowed() {
            return true;
        }

        @Override
        public String get() {
            return value;
        }

        @Override
        public String refresh() {
            return value;
        }

        @Override
        public String update(String datamarkValue) {
            value = datamarkValue;
            return value;
        }
    }
}
