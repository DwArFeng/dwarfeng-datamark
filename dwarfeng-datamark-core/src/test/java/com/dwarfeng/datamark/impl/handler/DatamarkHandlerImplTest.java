package com.dwarfeng.datamark.impl.handler;

import com.dwarfeng.datamark.stack.exception.IllegalDatamarkValueException;
import com.dwarfeng.datamark.stack.exception.UpdateNotAllowedException;
import com.dwarfeng.datamark.stack.struct.DatamarkConfig;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.WritableResource;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据标记处理器实现测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkHandlerImplTest {

    private static final String RESOURCE_URL = "memory:datamark";

    @Test
    public void readsCachesAndRefreshesDatamarkValue() throws Exception {
        MutableResource resource = new MutableResource("alpha");
        try (GenericApplicationContext context = context(resource)) {
            DatamarkHandlerImpl handler = new DatamarkHandlerImpl(
                    context, new DatamarkConfig(RESOURCE_URL, StandardCharsets.UTF_8.name(), false)
            );

            assertEquals("alpha", handler.get());
            resource.setText("beta");
            assertEquals("alpha", handler.get());
            assertEquals("beta", handler.refresh());
        }
    }

    @Test
    public void writesResourceAndUpdatesCache() throws Exception {
        MutableResource resource = new MutableResource("alpha");
        try (GenericApplicationContext context = context(resource)) {
            DatamarkHandlerImpl handler = new DatamarkHandlerImpl(
                    context, new DatamarkConfig(RESOURCE_URL, StandardCharsets.UTF_8.name(), true)
            );

            assertTrue(handler.updateAllowed());
            assertEquals("gamma", handler.update("gamma"));
            assertEquals("gamma", resource.text().strip());
            assertEquals("gamma", handler.get());
        }
    }

    @Test
    public void rejectsDisallowedAndInvalidUpdates() {
        MutableResource resource = new MutableResource("alpha");
        try (GenericApplicationContext context = context(resource)) {
            DatamarkHandlerImpl disallowedHandler = new DatamarkHandlerImpl(
                    context, new DatamarkConfig(RESOURCE_URL, StandardCharsets.UTF_8.name(), false)
            );
            DatamarkHandlerImpl allowedHandler = new DatamarkHandlerImpl(
                    context, new DatamarkConfig(RESOURCE_URL, StandardCharsets.UTF_8.name(), true)
            );

            assertThrows(UpdateNotAllowedException.class, () -> disallowedHandler.update("beta"));
            assertThrows(IllegalDatamarkValueException.class, () -> allowedHandler.update(""));
        }
    }

    private GenericApplicationContext context(MutableResource resource) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.addProtocolResolver((location, _) -> RESOURCE_URL.equals(location) ? resource : null);
        context.refresh();
        return context;
    }

    private static final class MutableResource extends AbstractResource implements WritableResource {

        private byte[] content;

        @SuppressWarnings("SameParameterValue")
        private MutableResource(String content) {
            setText(content);
        }

        @Override
        public @NonNull InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public @NonNull OutputStream getOutputStream() {
            return new ByteArrayOutputStream() {
                @Override
                public void close() throws IOException {
                    super.close();
                    content = toByteArray();
                }
            };
        }

        @Override
        public @NonNull String getDescription() {
            return RESOURCE_URL;
        }

        private String text() {
            return new String(content, StandardCharsets.UTF_8);
        }

        private void setText(String text) {
            content = text.getBytes(StandardCharsets.UTF_8);
        }
    }
}
