package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductOptionValueTest {

    @Test
    void equals_sameReference() {
        ProductOptionValue pov = ProductOptionValue.builder().id(1L).build();
        assertEquals(pov, pov);
    }

    @Test
    void equals_sameId() {
        ProductOptionValue pov1 = ProductOptionValue.builder().id(1L).build();
        ProductOptionValue pov2 = ProductOptionValue.builder().id(1L).build();
        assertEquals(pov1, pov2);
    }

    @Test
    void equals_differentId() {
        ProductOptionValue pov1 = ProductOptionValue.builder().id(1L).build();
        ProductOptionValue pov2 = ProductOptionValue.builder().id(2L).build();
        assertNotEquals(pov1, pov2);
    }

    @Test
    void equals_nullId() {
        ProductOptionValue pov1 = ProductOptionValue.builder().id(null).build();
        ProductOptionValue pov2 = ProductOptionValue.builder().id(1L).build();
        assertNotEquals(pov1, pov2);
    }

    @Test
    void equals_wrongType() {
        ProductOptionValue pov = ProductOptionValue.builder().id(1L).build();
        assertNotEquals(pov, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductOptionValue pov = ProductOptionValue.builder().id(1L).build();
        assertEquals(pov.hashCode(), ProductOptionValue.builder().id(2L).build().hashCode());
    }
}
