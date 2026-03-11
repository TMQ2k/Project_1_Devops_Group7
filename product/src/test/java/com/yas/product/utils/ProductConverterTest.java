package com.yas.product.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductConverterTest {

    @Test
    void toSlug_normalInput() {
        assertEquals("hello-world", ProductConverter.toSlug("Hello World"));
    }

    @Test
    void toSlug_withSpecialCharacters() {
        assertEquals("hello-world-123", ProductConverter.toSlug("Hello @World! 123"));
    }

    @Test
    void toSlug_withLeadingDash() {
        assertEquals("hello", ProductConverter.toSlug("@Hello"));
    }

    @Test
    void toSlug_withMultipleDashes() {
        assertEquals("hello-world", ProductConverter.toSlug("Hello---World"));
    }

    @Test
    void toSlug_trimmedInput() {
        assertEquals("hello", ProductConverter.toSlug("  Hello  "));
    }
}
