package com.dwarfeng.datamark.core.internal.i18n;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import com.dwarfeng.datamark.base.sdk.i18n.Messages;
import com.dwarfeng.datamark.base.stack.i18n.MessageCatalog;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Core 模块消息测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class CoreMessagesTest {

    @Test
    public void resolvesSimplifiedChineseMessagesFromEveryCatalog() {
        assertEquals(
                "非法的数据标记值: test",
                resolveWith(Locale.SIMPLIFIED_CHINESE, CoreMessageKey.EXCEPTION_ILLEGAL_DATAMARK_VALUE, "test")
        );
        assertEquals(
                "数据标记操作失败",
                CoreMessages.message(Locale.SIMPLIFIED_CHINESE, CoreMessageKey.SERVICE_EXCEPTION_DATAMARK_FAILED)
        );
        assertEquals(
                "test.Entity.field 字段中 @DatamarkField 注解的 handlerName 未指定（或为空字符串）, " +
                        "但应用上下文中存在多个 DatamarkHandler",
                CoreMessages.message(
                        Locale.SIMPLIFIED_CHINESE,
                        CoreMessageKey.ENTITY_LISTENER_AMBIGUOUS_HANDLER,
                        "test.Entity", "field"
                )
        );
        assertEquals(
                "获取数据标记值时发生异常",
                resolveWith(Locale.SIMPLIFIED_CHINESE, CoreMessageKey.SERVICE_GET_FAILED)
        );
    }

    @Test
    public void resolvesEnglishFallbackMessagesFromEveryCatalog() {
        assertEquals(
                "Illegal datamark value: test",
                CoreMessages.message(Locale.US, CoreMessageKey.EXCEPTION_ILLEGAL_DATAMARK_VALUE, "test")
        );
        assertEquals(
                "Datamark operation failed",
                resolveWith(Locale.US, CoreMessageKey.SERVICE_EXCEPTION_DATAMARK_FAILED)
        );
        assertEquals(
                "Field test.Entity.field: @DatamarkField handlerName is declared, resolved handler name is resolved, " +
                        "but no matching DatamarkHandler exists in the application context",
                CoreMessages.message(
                        Locale.US,
                        CoreMessageKey.ENTITY_LISTENER_HANDLER_NOT_FOUND,
                        "test.Entity", "field", "declared", "resolved"
                )
        );
        assertEquals(
                "An exception occurred while getting the datamark value",
                CoreMessages.message(Locale.US, CoreMessageKey.SERVICE_GET_FAILED)
        );
    }

    @Test
    public void degradesMissingMessageKeyForEveryCatalog() {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            assertEquals(
                    "!unknown.message.key!",
                    Messages.resolve(catalog.messageCatalog(), "unknown.message.key", Locale.US)
            );
        }
    }

    @Test
    public void keepsMessageKeyResourcesAlignedByCatalog() throws IOException {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            List<CoreMessageKey> catalogKeys = Arrays.stream(CoreMessageKey.values())
                    .filter(key -> key.catalog() == catalog)
                    .toList();
            Set<String> enumKeys = catalogKeys.stream()
                    .map(CoreMessageKey::key)
                    .collect(Collectors.toUnmodifiableSet());
            assertEquals(catalogKeys.size(), enumKeys.size(), () -> "Duplicate message key in catalog: " + catalog);
            assertEquals(enumKeys, loadKeys(catalog, ".properties"));
            assertEquals(enumKeys, loadKeys(catalog, "_zh_CN.properties"));
        }
    }

    @Test
    public void cachesOneMessageCatalogPerCatalog() {
        for (CoreMessages.Catalog catalog : CoreMessages.Catalog.values()) {
            MessageCatalog messageCatalog = catalog.messageCatalog();
            Arrays.stream(CoreMessageKey.values())
                    .filter(key -> key.catalog() == catalog)
                    .forEach(key -> assertSame(messageCatalog, key.catalog().messageCatalog()));
        }
    }

    private String resolveWith(Locale locale, CoreMessageKey key, Object... args) {
        AtomicReference<String> reference = new AtomicReference<>();
        MessageContext.run(locale, () -> reference.set(CoreMessages.message(key, args)));
        return reference.get();
    }

    private Set<String> loadKeys(CoreMessages.Catalog catalog, String resourceSuffix) throws IOException {
        String resourceName = catalog.messageCatalog().baseName().replace('.', '/') + resourceSuffix;
        try (InputStream inputStream = CoreMessages.class.getModule().getResourceAsStream(resourceName)) {
            assertNotNull(inputStream, () -> "Missing message resource: " + resourceName);
            Properties properties = new Properties();
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            return properties.stringPropertyNames();
        }
    }
}
