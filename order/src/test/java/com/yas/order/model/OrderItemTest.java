package com.yas.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void testOrderItemBuilder() {
        OrderItem orderItem = OrderItem.builder()
            .id(1L)
            .productId(100L)
            .orderId(200L)
            .productName("Test Product")
            .quantity(5)
            .productPrice(BigDecimal.valueOf(25.50))
            .note("Product note")
            .discountAmount(BigDecimal.valueOf(2.50))
            .taxAmount(BigDecimal.valueOf(1.25))
            .taxPercent(BigDecimal.valueOf(5.0))
            .build();

        assertThat(orderItem.getId()).isEqualTo(1L);
        assertThat(orderItem.getProductId()).isEqualTo(100L);
        assertThat(orderItem.getOrderId()).isEqualTo(200L);
        assertThat(orderItem.getProductName()).isEqualTo("Test Product");
        assertThat(orderItem.getQuantity()).isEqualTo(5);
        assertThat(orderItem.getProductPrice()).isEqualTo(BigDecimal.valueOf(25.50));
        assertThat(orderItem.getNote()).isEqualTo("Product note");
        assertThat(orderItem.getDiscountAmount()).isEqualTo(BigDecimal.valueOf(2.50));
        assertThat(orderItem.getTaxAmount()).isEqualTo(BigDecimal.valueOf(1.25));
        assertThat(orderItem.getTaxPercent()).isEqualTo(BigDecimal.valueOf(5.0));
    }

    @Test
    void testOrderItemSetters() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(2L);
        orderItem.setProductId(101L);
        orderItem.setOrderId(201L);
        orderItem.setProductName("Updated Product");
        orderItem.setQuantity(10);
        orderItem.setProductPrice(BigDecimal.valueOf(50.00));
        orderItem.setNote("Updated note");
        orderItem.setDiscountAmount(BigDecimal.valueOf(5.00));
        orderItem.setTaxAmount(BigDecimal.valueOf(2.50));
        orderItem.setTaxPercent(BigDecimal.valueOf(10.0));

        assertThat(orderItem.getId()).isEqualTo(2L);
        assertThat(orderItem.getProductId()).isEqualTo(101L);
        assertThat(orderItem.getOrderId()).isEqualTo(201L);
        assertThat(orderItem.getProductName()).isEqualTo("Updated Product");
        assertThat(orderItem.getQuantity()).isEqualTo(10);
        assertThat(orderItem.getProductPrice()).isEqualTo(BigDecimal.valueOf(50.00));
        assertThat(orderItem.getNote()).isEqualTo("Updated note");
        assertThat(orderItem.getDiscountAmount()).isEqualTo(BigDecimal.valueOf(5.00));
        assertThat(orderItem.getTaxAmount()).isEqualTo(BigDecimal.valueOf(2.50));
        assertThat(orderItem.getTaxPercent()).isEqualTo(BigDecimal.valueOf(10.0));
    }

    @Test
    void testOrderItemNoArgsConstructor() {
        OrderItem orderItem = new OrderItem();
        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getId()).isNull();
    }
}
