package com.yas.commonlibrary.kafka.cdc.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testBuilder() {
        Product product = Product.builder()
                .id(1L)
                .isPublished(true)
                .build();

        assertEquals(1L, product.getId());
        assertTrue(product.isPublished());
    }

    @Test
    void testSettersAndGetters() {
        Product product = new Product();
        product.setId(123L);
        product.setPublished(false);

        assertEquals(123L, product.getId());
        assertFalse(product.isPublished());
    }

    @Test
    void testAllArgsConstructor() {
        Product product = new Product(456L, true);

        assertEquals(456L, product.getId());
        assertTrue(product.isPublished());
    }

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();

        assertNotNull(product);
        assertEquals(0L, product.getId());
        assertFalse(product.isPublished());
    }
}
