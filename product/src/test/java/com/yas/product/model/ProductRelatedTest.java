package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductRelatedTest {

    @Test
    void equals_sameReference() {
        ProductRelated pr = ProductRelated.builder().id(1L).build();
        assertEquals(pr, pr);
    }

    @Test
    void equals_sameId() {
        ProductRelated pr1 = ProductRelated.builder().id(1L).build();
        ProductRelated pr2 = ProductRelated.builder().id(1L).build();
        assertEquals(pr1, pr2);
    }

    @Test
    void equals_differentId() {
        ProductRelated pr1 = ProductRelated.builder().id(1L).build();
        ProductRelated pr2 = ProductRelated.builder().id(2L).build();
        assertNotEquals(pr1, pr2);
    }

    @Test
    void equals_nullId() {
        ProductRelated pr1 = ProductRelated.builder().id(null).build();
        ProductRelated pr2 = ProductRelated.builder().id(1L).build();
        assertNotEquals(pr1, pr2);
    }

    @Test
    void equals_wrongType() {
        ProductRelated pr = ProductRelated.builder().id(1L).build();
        assertNotEquals(pr, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductRelated pr1 = ProductRelated.builder().id(1L).build();
        ProductRelated pr2 = ProductRelated.builder().id(2L).build();
        assertEquals(pr1.hashCode(), pr2.hashCode());
    }
}
