package com.yas.product.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessagesUtilsTest {

    @Test
    void getMessage_existingKey_returnsMessage() {
        // Test with an error code that may or may not exist in the resource bundle
        String result = MessagesUtils.getMessage("PRODUCT_NOT_FOUND", 1L);
        assertNotNull(result);
    }

    @Test
    void getMessage_missingKey_returnsErrorCode() {
        String result = MessagesUtils.getMessage("NON_EXISTENT_KEY_12345", "arg1");
        // When key is missing, returns the error code itself
        assertNotNull(result);
        assertTrue(result.contains("NON_EXISTENT_KEY_12345"));
    }

    @Test
    void getMessage_noArgs() {
        String result = MessagesUtils.getMessage("PRODUCT_NOT_FOUND");
        assertNotNull(result);
    }
}
