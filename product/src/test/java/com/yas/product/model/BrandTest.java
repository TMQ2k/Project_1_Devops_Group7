package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BrandTest {

    @Test
    void equals_sameReference() {
        Brand b = new Brand();
        b.setId(1L);
        assertEquals(b, b);
    }

    @Test
    void equals_sameId() {
        Brand b1 = new Brand();
        b1.setId(1L);
        Brand b2 = new Brand();
        b2.setId(1L);
        assertEquals(b1, b2);
    }

    @Test
    void equals_differentId() {
        Brand b1 = new Brand();
        b1.setId(1L);
        Brand b2 = new Brand();
        b2.setId(2L);
        assertNotEquals(b1, b2);
    }

    @Test
    void equals_nullId() {
        Brand b1 = new Brand();
        b1.setId(null);
        Brand b2 = new Brand();
        b2.setId(1L);
        assertNotEquals(b1, b2);
    }

    @Test
    void equals_wrongType() {
        Brand b = new Brand();
        b.setId(1L);
        assertNotEquals(b, "string");
    }

    @Test
    void hashCode_consistent() {
        Brand b1 = new Brand();
        b1.setId(1L);
        Brand b2 = new Brand();
        b2.setId(2L);
        assertEquals(b1.hashCode(), b2.hashCode());
    }
}
