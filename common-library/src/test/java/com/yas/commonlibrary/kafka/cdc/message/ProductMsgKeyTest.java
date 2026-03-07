package com.yas.commonlibrary.kafka.cdc.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMsgKeyTest {

    @Test
    void testBuilder() {
        ProductMsgKey key = ProductMsgKey.builder()
                .id(123L)
                .build();

        assertEquals(123L, key.getId());
    }

    @Test
    void testSettersAndGetters() {
        ProductMsgKey key = new ProductMsgKey();
        key.setId(456L);

        assertEquals(456L, key.getId());
    }

    @Test
    void testAllArgsConstructor() {
        ProductMsgKey key = new ProductMsgKey(789L);

        assertEquals(789L, key.getId());
    }

    @Test
    void testNoArgsConstructor() {
        ProductMsgKey key = new ProductMsgKey();

        assertNotNull(key);
        assertNull(key.getId());
    }
}
