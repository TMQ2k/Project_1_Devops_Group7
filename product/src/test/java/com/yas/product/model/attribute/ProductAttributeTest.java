package com.yas.product.model.attribute;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductAttributeTest {

    @Test
    void equals_sameReference() {
        ProductAttribute pa = ProductAttribute.builder().id(1L).build();
        assertEquals(pa, pa);
    }

    @Test
    void equals_sameId() {
        ProductAttribute pa1 = ProductAttribute.builder().id(1L).build();
        ProductAttribute pa2 = ProductAttribute.builder().id(1L).build();
        assertEquals(pa1, pa2);
    }

    @Test
    void equals_differentId() {
        ProductAttribute pa1 = ProductAttribute.builder().id(1L).build();
        ProductAttribute pa2 = ProductAttribute.builder().id(2L).build();
        assertNotEquals(pa1, pa2);
    }

    @Test
    void equals_nullId() {
        ProductAttribute pa1 = ProductAttribute.builder().id(null).build();
        ProductAttribute pa2 = ProductAttribute.builder().id(1L).build();
        assertNotEquals(pa1, pa2);
    }

    @Test
    void equals_wrongType() {
        ProductAttribute pa = ProductAttribute.builder().id(1L).build();
        assertNotEquals(pa, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductAttribute pa1 = ProductAttribute.builder().id(1L).build();
        ProductAttribute pa2 = ProductAttribute.builder().id(2L).build();
        assertEquals(pa1.hashCode(), pa2.hashCode());
    }
}
