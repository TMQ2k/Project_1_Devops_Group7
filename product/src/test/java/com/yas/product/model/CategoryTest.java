package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equals_sameReference() {
        Category c = new Category();
        c.setId(1L);
        assertEquals(c, c);
    }

    @Test
    void equals_sameId() {
        Category c1 = new Category();
        c1.setId(1L);
        Category c2 = new Category();
        c2.setId(1L);
        assertEquals(c1, c2);
    }

    @Test
    void equals_differentId() {
        Category c1 = new Category();
        c1.setId(1L);
        Category c2 = new Category();
        c2.setId(2L);
        assertNotEquals(c1, c2);
    }

    @Test
    void equals_nullId() {
        Category c1 = new Category();
        c1.setId(null);
        Category c2 = new Category();
        c2.setId(1L);
        assertNotEquals(c1, c2);
    }

    @Test
    void equals_wrongType() {
        Category c = new Category();
        c.setId(1L);
        assertNotEquals(c, "string");
    }

    @Test
    void hashCode_consistent() {
        Category c1 = new Category();
        c1.setId(1L);
        Category c2 = new Category();
        c2.setId(2L);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
