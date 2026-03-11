package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductOptionCombinationTest {

    @Test
    void equals_sameReference() {
        ProductOptionCombination poc = ProductOptionCombination.builder().id(1L).build();
        assertEquals(poc, poc);
    }

    @Test
    void equals_sameId() {
        ProductOptionCombination poc1 = ProductOptionCombination.builder().id(1L).build();
        ProductOptionCombination poc2 = ProductOptionCombination.builder().id(1L).build();
        assertEquals(poc1, poc2);
    }

    @Test
    void equals_differentId() {
        ProductOptionCombination poc1 = ProductOptionCombination.builder().id(1L).build();
        ProductOptionCombination poc2 = ProductOptionCombination.builder().id(2L).build();
        assertNotEquals(poc1, poc2);
    }

    @Test
    void equals_nullId() {
        ProductOptionCombination poc1 = ProductOptionCombination.builder().id(null).build();
        ProductOptionCombination poc2 = ProductOptionCombination.builder().id(1L).build();
        assertNotEquals(poc1, poc2);
    }

    @Test
    void equals_wrongType() {
        ProductOptionCombination poc = ProductOptionCombination.builder().id(1L).build();
        assertNotEquals(poc, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductOptionCombination poc = ProductOptionCombination.builder().id(1L).build();
        assertEquals(poc.hashCode(), ProductOptionCombination.builder().id(2L).build().hashCode());
    }
}
