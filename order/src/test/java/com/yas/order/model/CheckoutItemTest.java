package com.yas.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CheckoutItemTest {

    @Test
    void testCheckoutItemBuilder_whenNormalCase_createCheckoutItem() {
        Checkout checkout = Checkout.builder()
            .id("checkout-123")
            .build();

        CheckoutItem checkoutItem = CheckoutItem.builder()
            .id(1L)
            .productId(100L)
            .quantity(2)
            .description("Product description")
            .checkout(checkout)
            .build();

        assertThat(checkoutItem).isNotNull();
        assertThat(checkoutItem.getId()).isEqualTo(1L);
        assertThat(checkoutItem.getProductId()).isEqualTo(100L);
        assertThat(checkoutItem.getQuantity()).isEqualTo(2);
        assertThat(checkoutItem.getDescription()).isEqualTo("Product description");
        assertThat(checkoutItem.getCheckout()).isEqualTo(checkout);
    }

    @Test
    void testCheckoutItemSetters_whenNormalCase_updateCheckoutItem() {
        CheckoutItem checkoutItem = new CheckoutItem();
        Checkout checkout = new Checkout();
        checkout.setId("checkout-456");

        checkoutItem.setId(2L);
        checkoutItem.setProductId(200L);
        checkoutItem.setQuantity(5);
        checkoutItem.setDescription("Updated description");
        checkoutItem.setCheckout(checkout);

        assertThat(checkoutItem.getId()).isEqualTo(2L);
        assertThat(checkoutItem.getProductId()).isEqualTo(200L);
        assertThat(checkoutItem.getQuantity()).isEqualTo(5);
        assertThat(checkoutItem.getDescription()).isEqualTo("Updated description");
        assertThat(checkoutItem.getCheckout()).isEqualTo(checkout);
    }
}
