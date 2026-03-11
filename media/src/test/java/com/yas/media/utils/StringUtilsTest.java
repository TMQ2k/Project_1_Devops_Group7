package com.yas.media.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

    @Test
    void hasText_whenNull_shouldReturnFalse() {
        assertFalse(StringUtils.hasText(null));
    }

    @Test
    void hasText_whenEmpty_shouldReturnFalse() {
        assertFalse(StringUtils.hasText(""));
    }

    @Test
    void hasText_whenBlank_shouldReturnFalse() {
        assertFalse(StringUtils.hasText("   "));
    }

    @Test
    void hasText_whenHasContent_shouldReturnTrue() {
        assertTrue(StringUtils.hasText("hello"));
    }

    @Test
    void hasText_whenHasContentWithSpaces_shouldReturnTrue() {
        assertTrue(StringUtils.hasText("  hello  "));
    }
}
