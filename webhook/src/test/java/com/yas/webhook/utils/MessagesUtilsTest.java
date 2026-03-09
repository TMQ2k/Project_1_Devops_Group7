package com.yas.webhook.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesUtilsTest {

    @Test
    void testGetMessage_whenInvalidCode_thenReturnCode() {
        String message = MessagesUtils.getMessage("WEBHOOK_NOT_FOUND");
        assertEquals("WEBHOOK_NOT_FOUND", message);
    }

    @Test
    void testGetMessage_whenInvalidCodeWithParameter_thenReturnFormattedCode() {
        String message = MessagesUtils.getMessage("WEBHOOK_ERROR", 123);
        assertEquals("WEBHOOK_ERROR", message);
    }

    @Test
    void testGetMessage_whenInvalidCodeWithMultipleParameters_thenFormatWithPlaceholders() {
        String message = MessagesUtils.getMessage("ERROR_{}_IN_{}", "test", "module");
        assertEquals("ERROR_test_IN_module", message);
    }

    @Test
    void testGetMessage_whenNoParameters_thenReturnCode() {
        String message = MessagesUtils.getMessage("INVALID_EVENT");
        assertEquals("INVALID_EVENT", message);
    }
}
