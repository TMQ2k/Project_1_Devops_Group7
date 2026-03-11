package com.yas.media.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.yas.media.model.Media;
import com.yas.media.viewmodel.MediaVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaVmMapperImplTest {

    private MediaVmMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new MediaVmMapperImpl();
    }

    // --- toVm ---

    @Test
    void toVm_whenMediaIsNull_shouldReturnNull() {
        assertNull(mapper.toVm(null));
    }

    @Test
    void toVm_whenMediaIsValid_shouldMapFields() {
        Media media = new Media();
        media.setId(1L);
        media.setCaption("caption");
        media.setFileName("file.png");
        media.setMediaType("image/png");

        MediaVm result = mapper.toVm(media);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("caption", result.getCaption());
        assertEquals("file.png", result.getFileName());
        assertEquals("image/png", result.getMediaType());
        assertNull(result.getUrl());
    }

    // --- toModel ---

    @Test
    void toModel_whenVmIsNull_shouldReturnNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_whenVmIsValid_shouldMapFields() {
        MediaVm vm = new MediaVm(1L, "caption", "file.png", "image/png", "/url");

        Media result = mapper.toModel(vm);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("caption", result.getCaption());
        assertEquals("file.png", result.getFileName());
        assertEquals("image/png", result.getMediaType());
    }

    // --- partialUpdate ---

    @Test
    void partialUpdate_whenVmIsNull_shouldNotModifyMedia() {
        Media media = new Media();
        media.setId(1L);
        media.setCaption("original");

        mapper.partialUpdate(media, null);

        assertEquals(1L, media.getId());
        assertEquals("original", media.getCaption());
    }

    @Test
    void partialUpdate_whenVmHasValues_shouldUpdateMedia() {
        Media media = new Media();
        media.setId(1L);
        media.setCaption("original");
        media.setFileName("old.png");
        media.setMediaType("image/jpeg");

        MediaVm vm = new MediaVm(2L, "updated", "new.png", "image/png", "/url");

        mapper.partialUpdate(media, vm);

        assertEquals(2L, media.getId());
        assertEquals("updated", media.getCaption());
        assertEquals("new.png", media.getFileName());
        assertEquals("image/png", media.getMediaType());
    }

    @Test
    void partialUpdate_whenVmHasNullFields_shouldNotOverwrite() {
        Media media = new Media();
        media.setId(1L);
        media.setCaption("original");
        media.setFileName("file.png");
        media.setMediaType("image/png");

        MediaVm vm = new MediaVm(null, null, null, null, null);

        mapper.partialUpdate(media, vm);

        assertEquals(1L, media.getId());
        assertEquals("original", media.getCaption());
        assertEquals("file.png", media.getFileName());
        assertEquals("image/png", media.getMediaType());
    }
}
// just for recommit test, ignore this line