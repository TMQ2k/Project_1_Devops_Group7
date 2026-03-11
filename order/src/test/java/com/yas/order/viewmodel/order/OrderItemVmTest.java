package com.yas.order.viewmodel.order;

import com.yas.order.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemVmTest {

    @Test
    void testFromModel_whenOrderItemProvided_returnsOrderItemVm() {
        // Given
        OrderItem orderItem = OrderItem.builder()
            .id(1L)
            .productId(100L)
            .productName("Test Product")
            .quantity(5)
            .productPrice(new BigDecimal("29.99"))
            .note("Test note")
            .discountAmount(new BigDecimal("5.00"))
            .taxPercent(new BigDecimal("0.10"))
            .taxAmount(new BigDecimal("2.50"))
            .orderId(10L)
            .build();

        // When
        OrderItemVm result = OrderItemVm.fromModel(orderItem);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.productId()).isEqualTo(100L);
        assertThat(result.productName()).isEqualTo("Test Product");
        assertThat(result.quantity()).isEqualTo(5);
        assertThat(result.productPrice()).isEqualByComparingTo(new BigDecimal("29.99"));
        assertThat(result.note()).isEqualTo("Test note");
        assertThat(result.discountAmount()).isEqualByComparingTo(new BigDecimal("5.00"));
        assertThat(result.taxPercent()).isEqualByComparingTo(new BigDecimal("0.10"));
        assertThat(result.taxAmount()).isEqualByComparingTo(new BigDecimal("2.50"));
        assertThat(result.orderId()).isEqualTo(10L);
    }

    @Test
    void testBuilder_whenAllFieldsSet_returnsOrderItemVm() {
        // When
        OrderItemVm result = OrderItemVm.builder()
            .id(2L)
            .productId(200L)
            .productName("Another Product")
            .quantity(3)
            .productPrice(new BigDecimal("15.00"))
            .note("Another note")
            .discountAmount(new BigDecimal("3.00"))
            .taxPercent(new BigDecimal("0.08"))
            .taxAmount(new BigDecimal("1.20"))
            .orderId(20L)
            .build();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.productId()).isEqualTo(200L);
    }
}
