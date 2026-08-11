package com.dwarfeng.datamark.sdk.jpa;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import com.dwarfeng.datamark.stack.handler.DatamarkHandler;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 数据标记实体监听器测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkEntityListenerTest {

    @Test
    public void writesDatamarkThroughJavaBeanProperty() throws Exception {
        StubHandler handler = new StubHandler("alpha");
        DatamarkEntityListener listener = new DatamarkEntityListener(Map.of("handler", handler));
        TestEntity entity = new TestEntity();

        listener.prePersist(entity);
        assertEquals("alpha", entity.getDatamark());

        handler.setValue("beta");
        listener.preUpdate(entity);
        assertEquals("beta", entity.getDatamark());
    }

    @Test
    public void localizesMissingHandlerFailure() {
        DatamarkEntityListener listener = new DatamarkEntityListener(null);
        TestEntity entity = new TestEntity();

        IllegalStateException exception = MessageContext.call(
                Locale.ENGLISH,
                () -> assertThrows(IllegalStateException.class, () -> listener.prePersist(entity))
        );

        assertEquals("No DatamarkHandler exists in the application context", exception.getMessage());
    }

    @Test
    public void localizesAmbiguousAndUnknownHandlerFailures() {
        DatamarkEntityListener ambiguousListener = new DatamarkEntityListener(
                Map.of("alpha", new StubHandler("alpha"), "beta", new StubHandler("beta"))
        );
        DatamarkEntityListener unknownListener = new DatamarkEntityListener(
                Map.of("alpha", new StubHandler("alpha"))
        );

        IllegalStateException ambiguousException = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> ambiguousListener.prePersist(new TestEntity())
                )
        );
        IllegalStateException unknownException = MessageContext.call(
                Locale.ENGLISH,
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> unknownListener.prePersist(new NamedEntity())
                )
        );

        assertEquals(
                TestEntity.class.getCanonicalName() +
                        ".datamark 字段中 @DatamarkField 注解的 handlerName 未指定（或为空字符串）, " +
                        "但应用上下文中存在多个 DatamarkHandler",
                ambiguousException.getMessage()
        );
        assertEquals(
                "Field " + NamedEntity.class.getCanonicalName() +
                        ".datamark: @DatamarkField handlerName is missing, resolved handler name is missing, " +
                        "but no matching DatamarkHandler exists in the application context",
                unknownException.getMessage()
        );
    }

    public static class TestEntity {

        @DatamarkField
        private String datamark;

        public String getDatamark() {
            return datamark;
        }

        public void setDatamark(String datamark) {
            this.datamark = datamark;
        }
    }

    public static class NamedEntity {

        @DatamarkField(handlerName = "missing")
        private String datamark;

        public String getDatamark() {
            return datamark;
        }

        public void setDatamark(String datamark) {
            this.datamark = datamark;
        }
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

        @SuppressWarnings("SameParameterValue")
        private void setValue(String value) {
            this.value = value;
        }
    }
}
