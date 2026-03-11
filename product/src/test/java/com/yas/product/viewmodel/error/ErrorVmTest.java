package com.yas.product.viewmodel.error;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorVmTest {

    @Test
    void constructor_fourArgs_setsFieldErrors() {
        List<String> fieldErrors = List.of("field1 is required", "field2 is invalid");
        ErrorVm errorVm = new ErrorVm("400", "Bad Request", "Validation failed", fieldErrors);
        assertEquals("400", errorVm.statusCode());
        assertEquals("Bad Request", errorVm.title());
        assertEquals("Validation failed", errorVm.detail());
        assertEquals(2, errorVm.fieldErrors().size());
        assertEquals("field1 is required", errorVm.fieldErrors().get(0));
    }

    @Test
    void constructor_threeArgs_createsEmptyFieldErrors() {
        ErrorVm errorVm = new ErrorVm("404", "Not Found", "Product not found");
        assertEquals("404", errorVm.statusCode());
        assertEquals("Not Found", errorVm.title());
        assertEquals("Product not found", errorVm.detail());
        assertNotNull(errorVm.fieldErrors());
        assertTrue(errorVm.fieldErrors().isEmpty());
    }
}
