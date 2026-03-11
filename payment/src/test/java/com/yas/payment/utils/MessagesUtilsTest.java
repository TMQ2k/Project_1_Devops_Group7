package com.yas.payment.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MessagesUtilsTest {

    @Test
    void getMessage_withExistingKey_shouldReturnFormattedMessage() {
        String result = MessagesUtils.getMessage("PAYMENT_PROVIDER_NOT_FOUND", "testId");
        assertThat(result).isEqualTo("Payment provider testId is not found");
    }

    @Test
    void getMessage_withMissingKey_shouldReturnKeyAsMessage() {
        String result = MessagesUtils.getMessage("NON_EXISTENT_KEY");
        assertThat(result).isEqualTo("NON_EXISTENT_KEY");
    }

    @Test
    void getMessage_withSuccessMessage_shouldReturnSuccess() {
        String result = MessagesUtils.getMessage("SUCCESS_MESSAGE");
        assertThat(result).isEqualTo("SUCCESS");
    }

    @Test
    void constants_shouldBeAccessible() {
        assertThat(Constants.ErrorCode.PAYMENT_PROVIDER_NOT_FOUND).isNotNull();
        assertThat(Constants.Message.SUCCESS_MESSAGE).isNotNull();
    }
}
