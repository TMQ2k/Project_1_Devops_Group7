package com.yas.commonlibrary.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesUtilsTest {

    @Test
    void testGetMessage_whenValidCode_thenReturnMessage() {
        String message = MessagesUtils.getMessage("USER_NOT_FOUND");
        assertEquals("User not found", message);
    }

    @Test
    void testGetMessage_whenInvalidCode_thenReturnCode() {
        String message = MessagesUtils.getMessage("INVALID_CODE");
        assertEquals("INVALID_CODE", message);
    }

    @Test
    void testGetMessage_whenValidCodeWithOneParameter_thenReturnFormattedMessage() {
        String message = MessagesUtils.getMessage("PRODUCT_NOT_FOUND", 123);
        assertEquals("The product 123 is not found", message);
    }

    @Test
    void testGetMessage_whenValidCodeWithMultipleParameters_thenReturnFormattedMessage() {
        String message = MessagesUtils.getMessage("USER_WITH_EMAIL_NOT_FOUND", "test@example.com");
        assertEquals("User with email test@example.com not found", message);
    }
}
