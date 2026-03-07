package com.yas.commonlibrary.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateTimeUtilsTest {

    @Test
    void testFormat_whenDefaultPattern_thenReturnFormattedString() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 10, 30, 45);
        String result = DateTimeUtils.format(dateTime);

        assertNotNull(result);
        assertEquals("15-03-2024_10-30-45", result);
    }

    @Test
    void testFormat_whenCustomPattern_thenReturnFormattedString() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 10, 30, 45);
        String result = DateTimeUtils.format(dateTime, "yyyy/MM/dd HH:mm:ss");

        assertNotNull(result);
        assertEquals("2024/03/15 10:30:45", result);
    }

    @Test
    void testFormat_whenDifferentCustomPattern_thenReturnFormattedString() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 12, 25, 23, 59, 59);
        String result = DateTimeUtils.format(dateTime, "dd.MM.yyyy");

        assertNotNull(result);
        assertEquals("25.12.2024", result);
    }
}
