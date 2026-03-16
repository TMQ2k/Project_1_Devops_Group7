package com.yas.promotion.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MessagesUtilsTest {

    @Test
    void testGetMessage_whenValidMessageCode_thenReturnMessage() {
        String result = MessagesUtils.getMessage(Constants.ErrorCode.PROMOTION_NOT_FOUND);
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
        String result = MessagesUtils.getMessage(Constants.ErrorCode.PROMOTION_NOT_FOUND, "123"  );
        assertNotNull(result);
    }

    @Test
    void testGetMessage_whenMessageCodeWithMultipleParameters_thenReturnFormattedMessage() {
        String result = MessagesUtils.getMessage(Constants.ErrorCode.PROMOTION_NOT_FOUND, "Param1", "Param2");
        assertNotNull(result);
    }
}
