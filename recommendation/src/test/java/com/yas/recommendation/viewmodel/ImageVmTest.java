package com.yas.recommendation.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageVmTest {

    @Test
    void testRecordConstructor() {
        ImageVm imageVm = new ImageVm(1L, "http://example.com/image.jpg");

        assertEquals(1L, imageVm.id());
        assertEquals("http://example.com/image.jpg", imageVm.url());
    }

    @Test
    void testRecordEquality() {
        ImageVm imageVm1 = new ImageVm(1L, "http://example.com/image.jpg");
        ImageVm imageVm2 = new ImageVm(1L, "http://example.com/image.jpg");
        ImageVm imageVm3 = new ImageVm(2L, "http://example.com/other.jpg");

        assertEquals(imageVm1, imageVm2);
        assertNotEquals(imageVm1, imageVm3);
    }

    @Test
    void testRecordHashCode() {
        ImageVm imageVm1 = new ImageVm(1L, "http://example.com/image.jpg");
        ImageVm imageVm2 = new ImageVm(1L, "http://example.com/image.jpg");

        assertEquals(imageVm1.hashCode(), imageVm2.hashCode());
    }

    @Test
    void testNullValues() {
        ImageVm imageVm = new ImageVm(null, null);

        assertNull(imageVm.id());
        assertNull(imageVm.url());
    }
}
