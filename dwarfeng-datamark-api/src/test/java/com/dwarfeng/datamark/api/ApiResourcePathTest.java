package com.dwarfeng.datamark.api;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * API 测试资源路径测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class ApiResourcePathTest {

    @Test
    public void keepsDatamarkResourcesInApiDatamarkDirectory() throws IOException {
        assertResourceExists("com/dwarfeng/datamark/api/datamark/default.storage");
        assertResourceExists("com/dwarfeng/datamark/api/datamark/settings.properties");
    }

    private void assertResourceExists(String resourceName) throws IOException {
        try (InputStream inputStream = ApiResourcePathTest.class.getModule().getResourceAsStream(resourceName)) {
            assertNotNull(inputStream);
        }
    }
}
