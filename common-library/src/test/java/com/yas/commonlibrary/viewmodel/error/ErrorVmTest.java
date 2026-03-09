package com.yas.commonlibrary.viewmodel.error;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorVmTest {

    @Test
    void testConstructorWithAllParameters() {
        List<String> fieldErrors = List.of("error1", "error2");
        ErrorVm errorVm = new ErrorVm("404", "Not Found", "Resource not found", fieldErrors);

        assertEquals("404", errorVm.statusCode());
        assertEquals("Not Found", errorVm.title());
        assertEquals("Resource not found", errorVm.detail());
        assertEquals(fieldErrors, errorVm.fieldErrors());
    }

    @Test
    void testConstructorWithoutFieldErrors() {
        ErrorVm errorVm = new ErrorVm("500", "Internal Error", "Something went wrong");

        assertEquals("500", errorVm.statusCode());
        assertEquals("Internal Error", errorVm.title());
        assertEquals("Something went wrong", errorVm.detail());
        assertNotNull(errorVm.fieldErrors());
        assertTrue(errorVm.fieldErrors() instanceof ArrayList);
        assertTrue(errorVm.fieldErrors().isEmpty());
    }

    @Test
    void testFieldErrorsIsModifiable() {
        ErrorVm errorVm = new ErrorVm("400", "Bad Request", "Invalid input");
        errorVm.fieldErrors().add("field error");

        assertEquals(1, errorVm.fieldErrors().size());
        assertEquals("field error", errorVm.fieldErrors().getFirst());
    }
}
