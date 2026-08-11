package com.dwarfeng.datamark.stack.struct;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatamarkConfig 兼容性测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkConfigTest {

    @Test
    public void preservesConstructorAndJavaBeanGetters() {
        DatamarkConfig config = new DatamarkConfig("classpath:test.storage", "UTF-8", true);

        assertFalse(DatamarkConfig.class.isRecord());
        assertEquals("classpath:test.storage", config.getResourceUrl());
        assertEquals("UTF-8", config.getResourceCharset());
        assertTrue(config.isUpdateAllowed());
    }

    @Test
    public void usesJdk25CharsetAndPackagedDefaultResource() throws IOException {
        DatamarkConfig config = new DatamarkConfig.Builder().build();

        assertEquals(
                "classpath:com/dwarfeng/datamark/node/datamark/default.storage",
                DatamarkConfig.Builder.DEFAULT_RESOURCE_URL
        );
        assertEquals(DatamarkConfig.Builder.DEFAULT_RESOURCE_URL, config.getResourceUrl());
        assertEquals(StandardCharsets.UTF_8.name(), config.getResourceCharset());
        assertFalse(config.isUpdateAllowed());

        String resourceName = config.getResourceUrl().substring("classpath:".length());
        assertResourceExists(resourceName);
    }

    @Test
    public void keepsDatamarkResourcesInNodeDatamarkDirectory() throws IOException {
        assertResourceExists("com/dwarfeng/datamark/node/datamark/default.storage");
        assertResourceExists("com/dwarfeng/datamark/node/datamark/multiton/settings.properties");
        assertResourceExists("com/dwarfeng/datamark/node/datamark/singleton/settings.properties");
    }

    private void assertResourceExists(String resourceName) throws IOException {
        try (InputStream inputStream = DatamarkConfig.class.getModule().getResourceAsStream(resourceName)) {
            assertNotNull(inputStream);
        }
    }
}
