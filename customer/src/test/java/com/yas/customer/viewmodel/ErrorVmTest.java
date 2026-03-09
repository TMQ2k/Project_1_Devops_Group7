package com.yas.customer.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ErrorVmTest {

    @Test
    void testConstructorWithAllParameters() {
        List<String> fieldErrors = List.of("error1", "error2");
        ErrorVm errorVm = new ErrorVm("400", "Bad Request", "Invalid input", fieldErrors);
        
        assertEquals("400", errorVm.statusCode());
        assertEquals("Bad Request", errorVm.title());
        assertEquals("Invalid input", errorVm.detail());
        assertEquals(2, errorVm.fieldErrors().size());
        assertTrue(errorVm.fieldErrors().contains("error1"));
    }

    @Test
    void testConstructorWithoutFieldErrors() {
        ErrorVm errorVm = new ErrorVm("404", "Not Found", "Resource not found");
        
        assertEquals("404", errorVm.statusCode());
        assertEquals("Not Found", errorVm.title());
        assertEquals("Resource not found", errorVm.detail());
        assertNotNull(errorVm.fieldErrors());
        assertTrue(errorVm.fieldErrors().isEmpty());
    }

    @Test
    void testConstructorCreatesEmptyListForFieldErrors() {
        ErrorVm errorVm = new ErrorVm("500", "Internal Error", "Server error");
        assertEquals(ArrayList.class, errorVm.fieldErrors().getClass());
    }
}
