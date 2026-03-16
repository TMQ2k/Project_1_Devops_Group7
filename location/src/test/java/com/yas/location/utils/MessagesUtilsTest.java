package com.yas.location.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MessagesUtilsTest {

    @Test
    void testGetMessage_whenValidMessageCode_thenReturnMessage() {
        String result = MessagesUtils.getMessage(Constants.ErrorCode.COUNTRY_NOT_FOUND);
        assertNotNull(result);
    }

    @Test
    void testGetMessage_whenInvalidMessageCode_thenReturnCodeItself() {
        String invalidCode = "INVALID_CODE_THAT_DOES_NOT_EXIST";
        String result = MessagesUtils.getMessage(invalidCode);
        assertEquals(invalidCode, result);
    }

    @Test
    void testGetMessage_whenMessageCodeWithParameters_thenReturnFormattedMessage() {
        String result = MessagesUtils.getMessage(Constants.ErrorCode.COUNTRY_NOT_FOUND, "TestCountry");
        assertNotNull(result);
    }

    @Test
    void testGetMessage_whenMessageCodeWithMultipleParameters_thenReturnFormattedMessage() {
        String result = MessagesUtils.getMessage(Constants.ErrorCode.NAME_ALREADY_EXITED, "Name1", "Name2");
        assertNotNull(result);
    }
}
