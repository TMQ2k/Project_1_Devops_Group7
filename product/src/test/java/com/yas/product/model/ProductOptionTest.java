package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductOptionTest {

    @Test
    void equals_sameReference() {
        ProductOption po = new ProductOption();
        po.setId(1L);
        assertEquals(po, po);
    }

    @Test
    void equals_sameId() {
        ProductOption po1 = new ProductOption();
        po1.setId(1L);
        ProductOption po2 = new ProductOption();
        po2.setId(1L);
        assertEquals(po1, po2);
    }

    @Test
    void equals_differentId() {
        ProductOption po1 = new ProductOption();
        po1.setId(1L);
        ProductOption po2 = new ProductOption();
        po2.setId(2L);
        assertNotEquals(po1, po2);
    }

    @Test
    void equals_nullId() {
        ProductOption po1 = new ProductOption();
        po1.setId(null);
        ProductOption po2 = new ProductOption();
        po2.setId(1L);
        assertNotEquals(po1, po2);
    }

    @Test
    void equals_wrongType() {
        ProductOption po = new ProductOption();
        po.setId(1L);
        assertNotEquals(po, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductOption po1 = new ProductOption();
        po1.setId(1L);
        ProductOption po2 = new ProductOption();
        po2.setId(2L);
        assertEquals(po1.hashCode(), po2.hashCode());
    }
}
