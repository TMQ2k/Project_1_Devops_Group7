package com.yas.promotion.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class AbstractCircuitBreakFallbackHandlerTest {

    // Test wrapper to expose protected methods
    static class TestCircuitBreakHandler extends AbstractCircuitBreakFallbackHandler {
        public void testHandleBodilessFallback(List<Long> ids, Throwable throwable) throws Throwable {
            handleBodilessFallback(ids, throwable);
        }

        public Object testHandleFallback(List<Long> ids, Throwable throwable) throws Throwable {
            return handleFallback(ids, throwable);
        }
    }

    @Test
    void testHandleBodilessFallback_whenException_thenThrow() {
        TestCircuitBreakHandler handler = new TestCircuitBreakHandler();
        RuntimeException exception = new RuntimeException("Test exception");

        assertThrows(RuntimeException.class, 
            () -> handler.testHandleBodilessFallback(List.of(1L, 2L), exception));
    }

    @Test
    void testHandleFallback_whenException_thenThrow() {
        TestCircuitBreakHandler handler = new TestCircuitBreakHandler();
        RuntimeException exception = new RuntimeException("Test exception");

        assertThrows(RuntimeException.class,
            () -> handler.testHandleFallback(List.of(3L, 4L), exception));
    }
}
