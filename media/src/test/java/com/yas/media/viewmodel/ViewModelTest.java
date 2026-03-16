package com.yas.media.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ViewModelTest {

    @Test
    void noFileMediaVm_shouldHoldValues() {
        NoFileMediaVm vm = new NoFileMediaVm(1L, "caption", "file.png", "image/png");
        assertEquals(1L, vm.id());
        assertEquals("caption", vm.caption());
        assertEquals("file.png", vm.fileName());
        assertEquals("image/png", vm.mediaType());
    }

    @Test
    void errorVm_withFieldErrors_shouldHoldValues() {
        List<String> errors = List.of("field1 is required");
        ErrorVm vm = new ErrorVm("400", "Bad Request", "Validation failed", errors);
        assertEquals("400", vm.statusCode());
        assertEquals("Bad Request", vm.title());
        assertEquals("Validation failed", vm.detail());
        assertEquals(1, vm.fieldErrors().size());
    }

    @Test
    void errorVm_withoutFieldErrors_shouldCreateEmptyList() {
        ErrorVm vm = new ErrorVm("404", "Not Found", "Resource not found");
        assertEquals("404", vm.statusCode());
        assertEquals("Not Found", vm.title());
        assertEquals("Resource not found", vm.detail());
        assertNotNull(vm.fieldErrors());
        assertTrue(vm.fieldErrors().isEmpty());
    }

    @Test
    void mediaVm_shouldHoldValues() {
        MediaVm vm = new MediaVm(1L, "caption", "file.png", "image/png", "/url");
        assertEquals(1L, vm.getId());
        assertEquals("caption", vm.getCaption());
        assertEquals("file.png", vm.getFileName());
        assertEquals("image/png", vm.getMediaType());
        assertEquals("/url", vm.getUrl());
    }

    @Test
    void mediaVm_setters_shouldWork() {
        MediaVm vm = new MediaVm(1L, "caption", "file.png", "image/png", "/url");
        vm.setId(2L);
        vm.setCaption("new caption");
        vm.setFileName("new.png");
        vm.setMediaType("image/gif");
        vm.setUrl("/new-url");

        assertEquals(2L, vm.getId());
        assertEquals("new caption", vm.getCaption());
        assertEquals("new.png", vm.getFileName());
        assertEquals("image/gif", vm.getMediaType());
        assertEquals("/new-url", vm.getUrl());
    }
}
