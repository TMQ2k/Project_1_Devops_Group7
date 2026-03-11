package com.yas.product.model.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductAttributeGroupTest {

    @Test
    void equals_sameReference() {
        ProductAttributeGroup pag = new ProductAttributeGroup();
        pag.setId(1L);
        assertEquals(pag, pag);
    }

    @Test
    void equals_sameId() {
        ProductAttributeGroup pag1 = new ProductAttributeGroup();
        pag1.setId(1L);
        ProductAttributeGroup pag2 = new ProductAttributeGroup();
        pag2.setId(1L);
        assertEquals(pag1, pag2);
    }

    @Test
    void equals_differentId() {
        ProductAttributeGroup pag1 = new ProductAttributeGroup();
        pag1.setId(1L);
        ProductAttributeGroup pag2 = new ProductAttributeGroup();
        pag2.setId(2L);
        assertNotEquals(pag1, pag2);
    }

    @Test
    void equals_nullId() {
        ProductAttributeGroup pag1 = new ProductAttributeGroup();
        pag1.setId(null);
        ProductAttributeGroup pag2 = new ProductAttributeGroup();
        pag2.setId(1L);
        assertNotEquals(pag1, pag2);
    }

    @Test
    void equals_wrongType() {
        ProductAttributeGroup pag = new ProductAttributeGroup();
        pag.setId(1L);
        assertNotEquals(pag, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductAttributeGroup pag1 = new ProductAttributeGroup();
        pag1.setId(1L);
        ProductAttributeGroup pag2 = new ProductAttributeGroup();
        pag2.setId(2L);
        assertEquals(pag1.hashCode(), pag2.hashCode());
    }
}
