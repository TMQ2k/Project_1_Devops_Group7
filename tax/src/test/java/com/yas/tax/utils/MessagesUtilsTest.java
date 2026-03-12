package com.yas.tax.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MessagesUtilsTest {

    @Test
    void getMessage_whenKeyExists_shouldReturnFormattedMessage() {
        String result = MessagesUtils.getMessage("TAX_CLASS_NOT_FOUND", 1L);
        assertThat(result).isNotNull();
    }

    @Test
    void getMessage_whenKeyNotExists_shouldReturnKeyAsMessage() {
        String result = MessagesUtils.getMessage("NON_EXISTENT_KEY");
        assertThat(result).isEqualTo("NON_EXISTENT_KEY");
    }

    @Test
    void getMessage_withMultipleArgs_shouldFormat() {
        String result = MessagesUtils.getMessage("UNKNOWN_CODE", "arg1", "arg2");
        assertThat(result).isNotNull();
    }
}
