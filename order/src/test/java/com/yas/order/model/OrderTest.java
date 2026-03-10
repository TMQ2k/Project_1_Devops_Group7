package com.yas.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.order.model.enumeration.DeliveryMethod;
import com.yas.order.model.enumeration.DeliveryStatus;
import com.yas.order.model.enumeration.OrderStatus;
import com.yas.order.model.enumeration.PaymentStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void testOrderBuilder() {
        OrderAddress shippingAddress = OrderAddress.builder()
            .contactName("John Doe")
            .phone("1234567890")
            .build();

        OrderAddress billingAddress = OrderAddress.builder()
            .contactName("Jane Doe")
            .phone("0987654321")
            .build();

        Order order = Order.builder()
            .id(1L)
            .email("test@example.com")
            .shippingAddressId(shippingAddress)
            .billingAddressId(billingAddress)
            .note("Test note")
            .tax(10.5f)
            .discount(5.0f)
            .numberItem(3)
            .couponCode("SUMMER20")
            .totalPrice(BigDecimal.valueOf(100.00))
            .deliveryFee(BigDecimal.valueOf(15.00))
            .orderStatus(OrderStatus.PENDING)
            .deliveryMethod(DeliveryMethod.VIETTEL_POST)
            .deliveryStatus(DeliveryStatus.PREPARING)
            .paymentStatus(PaymentStatus.PENDING)
            .checkoutId("checkout123")
            .build();

        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getEmail()).isEqualTo("test@example.com");
        assertThat(order.getNote()).isEqualTo("Test note");
        assertThat(order.getTax()).isEqualTo(10.5f);
        assertThat(order.getDiscount()).isEqualTo(5.0f);
        assertThat(order.getNumberItem()).isEqualTo(3);
        assertThat(order.getCouponCode()).isEqualTo("SUMMER20");
        assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(order.getDeliveryFee()).isEqualTo(BigDecimal.valueOf(15.00));
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getDeliveryMethod()).isEqualTo(DeliveryMethod.VIETTEL_POST);
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.PREPARING);
        assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(order.getCheckoutId()).isEqualTo("checkout123");
        assertThat(order.getShippingAddressId()).isEqualTo(shippingAddress);
        assertThat(order.getBillingAddressId()).isEqualTo(billingAddress);
    }

    @Test
    void testOrderSetters() {
        Order order = new Order();
        order.setId(2L);
        order.setEmail("updated@example.com");
        order.setNote("Updated note");
        order.setTax(12.5f);
        order.setDiscount(7.0f);
        order.setNumberItem(5);
        order.setCouponCode("WINTER25");
        order.setTotalPrice(BigDecimal.valueOf(200.00));
        order.setDeliveryFee(BigDecimal.valueOf(20.00));
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setDeliveryMethod(DeliveryMethod.GRAB_EXPRESS);
        order.setDeliveryStatus(DeliveryStatus.DELIVERING);
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        order.setCheckoutId("checkout456");
        order.setRejectReason("Out of stock");

        assertThat(order.getId()).isEqualTo(2L);
        assertThat(order.getEmail()).isEqualTo("updated@example.com");
        assertThat(order.getNote()).isEqualTo("Updated note");
        assertThat(order.getTax()).isEqualTo(12.5f);
        assertThat(order.getDiscount()).isEqualTo(7.0f);
        assertThat(order.getNumberItem()).isEqualTo(5);
        assertThat(order.getCouponCode()).isEqualTo("WINTER25");
        assertThat(order.getTotalPrice()).isEqualTo(BigDecimal.valueOf(200.00));
        assertThat(order.getDeliveryFee()).isEqualTo(BigDecimal.valueOf(20.00));
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
        assertThat(order.getDeliveryMethod()).isEqualTo(DeliveryMethod.GRAB_EXPRESS);
        assertThat(order.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERING);
        assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(order.getCheckoutId()).isEqualTo("checkout456");
        assertThat(order.getRejectReason()).isEqualTo("Out of stock");
    }

    @Test
    void testOrderNoArgsConstructor() {
        Order order = new Order();
        assertThat(order).isNotNull();
        assertThat(order.getId()).isNull();
    }
}
