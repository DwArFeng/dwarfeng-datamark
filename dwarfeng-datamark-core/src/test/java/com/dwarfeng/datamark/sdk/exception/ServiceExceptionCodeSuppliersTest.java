package com.dwarfeng.datamark.sdk.exception;

import com.dwarfeng.datamark.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * 数据标记模块异常代码供应器测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldResolveTipForCurrentMessageContext() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH, () -> new ServiceException(ServiceExceptionCodeSuppliers.DATAMARK_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.DATAMARK_FAILED.get())
        );

        assertEquals("Datamark operation failed", english.getCode().getTip());
        assertEquals("数据标记操作失败", chinese.getCode().getTip());
    }

    @Test
    public void shouldPreserveAllCodeOffsetsAndCreateNewCodes() {
        int offset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        for (CodeCase codeCase : codeCases()) {
            ServiceException.Code first = codeCase.supplier().get();
            ServiceException.Code second = codeCase.supplier().get();

            assertEquals(offset + codeCase.delta(), first.getCode());
            assertEquals(offset + codeCase.delta(), second.getCode());
            assertNotSame(first, second);
        }
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodesOnly() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        ServiceException.Code original = ServiceExceptionCodeSuppliers.DATAMARK_FAILED.get();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            ServiceException.Code updated = ServiceExceptionCodeSuppliers.DATAMARK_FAILED.get();

            assertEquals(previousOffset, original.getCode());
            assertEquals(9000, updated.getCode());
            assertNotSame(original, updated);
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }

    private List<CodeCase> codeCases() {
        return List.of(
                new CodeCase(ServiceExceptionCodeSuppliers.DATAMARK_FAILED, 0),
                new CodeCase(ServiceExceptionCodeSuppliers.ILLEGAL_DATAMARK_VALUE, 1),
                new CodeCase(ServiceExceptionCodeSuppliers.RESOURCE_NOT_WRITABLE, 2),
                new CodeCase(ServiceExceptionCodeSuppliers.RESOURCE_READ_FAILED, 3),
                new CodeCase(ServiceExceptionCodeSuppliers.RESOURCE_WRITE_FAILED, 4),
                new CodeCase(ServiceExceptionCodeSuppliers.UPDATE_NOT_ALLOWED, 5),
                new CodeCase(ServiceExceptionCodeSuppliers.DATAMARK_QOS_FAILED, 10),
                new CodeCase(ServiceExceptionCodeSuppliers.AMBIGUOUS_DATAMARK_HANDLER, 11),
                new CodeCase(ServiceExceptionCodeSuppliers.NO_DATAMARK_HANDLER_PRESENT, 12),
                new CodeCase(ServiceExceptionCodeSuppliers.DATAMARK_HANDLER_NOT_FOUND, 13),
                new CodeCase(ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_FAILED, 20),
                new CodeCase(ServiceExceptionCodeSuppliers.AMBIGUOUS_LISTENER_RESOLVER, 21),
                new CodeCase(ServiceExceptionCodeSuppliers.NO_LISTENER_RESOLVER_PRESENT, 22),
                new CodeCase(ServiceExceptionCodeSuppliers.LISTENER_RESOLVER_NOT_FOUND, 23)
        );
    }

    private record CodeCase(Supplier<ServiceException.Code> supplier, int delta) {
    }
}
