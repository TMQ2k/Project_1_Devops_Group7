package com.yas.payment.paypal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCircuitBreakFallbackHandlerTest {

    private AbstractCircuitBreakFallbackHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AbstractCircuitBreakFallbackHandler() {};
    }

    @Test
    void handleBodilessFallback_throwsSameException() {
        RuntimeException exception = new RuntimeException("test error");
        Throwable thrown = assertThrows(RuntimeException.class,
                () -> handler.handleBodilessFallback(exception));
        assertEquals("test error", thrown.getMessage());
        assertSame(exception, thrown);
    }

    @Test
    void handleTypedFallback_throwsSameException() {
        RuntimeException exception = new RuntimeException("typed error");
        Throwable thrown = assertThrows(RuntimeException.class,
                () -> handler.handleTypedFallback(exception));
        assertEquals("typed error", thrown.getMessage());
        assertSame(exception, thrown);
    }
}
