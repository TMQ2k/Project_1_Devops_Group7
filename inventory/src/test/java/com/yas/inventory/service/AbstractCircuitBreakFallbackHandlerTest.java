package com.yas.inventory.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AbstractCircuitBreakFallbackHandlerTest {

    private final TestHandler handler = new TestHandler();

    @Test
    void handleBodilessFallback_shouldRethrowException() {
        RuntimeException ex = new RuntimeException("test error");

        assertThrows(Throwable.class, () -> handler.handleBodilessFallback(ex));
    }

    @Test
    void handleTypedFallback_shouldRethrowException() {
        RuntimeException ex = new RuntimeException("test error");

        assertThrows(Throwable.class, () -> handler.handleTypedFallback(ex));
    }

    // Concrete subclass to test the abstract handler
    static class TestHandler extends AbstractCircuitBreakFallbackHandler {
    }
}
