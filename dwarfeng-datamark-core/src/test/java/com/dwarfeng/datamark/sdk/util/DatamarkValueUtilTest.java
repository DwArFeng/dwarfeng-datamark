package com.dwarfeng.datamark.sdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 数据标记值工具类测试。
 *
 * @author DwArFeng
 * @since 3.0.0
 */
public class DatamarkValueUtilTest {

    @Test
    public void acceptsValidDatamarkValues() {
        assertTrue(DatamarkValueUtil.isDatamarkValueValid("a"));
        assertTrue(DatamarkValueUtil.isDatamarkValueValid("alpha beta"));
        assertTrue(DatamarkValueUtil.isDatamarkValueValid("x".repeat(Constraints.LENGTH_DATAMARK_VALUE)));
    }

    @Test
    public void rejectsInvalidDatamarkValues() {
        assertFalse(DatamarkValueUtil.isDatamarkValueValid(null));
        assertFalse(DatamarkValueUtil.isDatamarkValueValid(""));
        assertFalse(DatamarkValueUtil.isDatamarkValueValid(" alpha"));
        assertFalse(DatamarkValueUtil.isDatamarkValueValid("alpha "));
        assertFalse(DatamarkValueUtil.isDatamarkValueValid("alpha\tbeta"));
        assertFalse(DatamarkValueUtil.isDatamarkValueValid("x".repeat(Constraints.LENGTH_DATAMARK_VALUE + 1)));
    }
}
