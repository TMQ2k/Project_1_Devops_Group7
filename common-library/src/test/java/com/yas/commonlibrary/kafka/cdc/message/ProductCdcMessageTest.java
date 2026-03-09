package com.yas.commonlibrary.kafka.cdc.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCdcMessageTest {

    @Test
    void testBuilder() {
        Product before = Product.builder().id(1L).isPublished(false).build();
        Product after = Product.builder().id(1L).isPublished(true).build();

        ProductCdcMessage message = ProductCdcMessage.builder()
                .before(before)
                .after(after)
                .op(Operation.UPDATE)
                .build();

        assertEquals(before, message.getBefore());
        assertEquals(after, message.getAfter());
        assertEquals(Operation.UPDATE, message.getOp());
    }

    @Test
    void testSettersAndGetters() {
        Product product = Product.builder().id(2L).isPublished(true).build();
        ProductCdcMessage message = new ProductCdcMessage();
        
        message.setAfter(product);
        message.setOp(Operation.CREATE);

        assertEquals(product, message.getAfter());
        assertNull(message.getBefore());
        assertEquals(Operation.CREATE, message.getOp());
    }

    @Test
    void testAllArgsConstructor() {
        Product before = new Product(1L, false);
        Product after = new Product(1L, true);

        ProductCdcMessage message = new ProductCdcMessage(after, before, Operation.UPDATE);

        assertEquals(after, message.getAfter());
        assertEquals(before, message.getBefore());
        assertEquals(Operation.UPDATE, message.getOp());
    }

    @Test
    void testNoArgsConstructor() {
        ProductCdcMessage message = new ProductCdcMessage();

        assertNotNull(message);
        assertNull(message.getAfter());
        assertNull(message.getBefore());
        assertNull(message.getOp());
    }

    @Test
    void testDeleteOperation() {
        Product before = Product.builder().id(1L).isPublished(true).build();

        ProductCdcMessage message = ProductCdcMessage.builder()
                .before(before)
                .op(Operation.DELETE)
                .build();

        assertEquals(before, message.getBefore());
        assertNull(message.getAfter());
        assertEquals(Operation.DELETE, message.getOp());
    }
}
