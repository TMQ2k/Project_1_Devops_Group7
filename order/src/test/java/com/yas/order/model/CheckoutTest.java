package com.yas.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.order.model.enumeration.CheckoutState;
import org.junit.jupiter.api.Test;

class CheckoutTest {

    @Test
    void testCheckoutBuilder_whenNormalCase_createCheckout() {
        Checkout checkout = Checkout.builder()
            .id("checkout-123")
            .email("test@example.com")
            .note("Test note")
            .promotionCode("DISCOUNT10")
            .checkoutState(CheckoutState.PENDING)
            .build();

        assertThat(checkout).isNotNull();
        assertThat(checkout.getId()).isEqualTo("checkout-123");
        assertThat(checkout.getEmail()).isEqualTo("test@example.com");
        assertThat(checkout.getNote()).isEqualTo("Test note");
        assertThat(checkout.getPromotionCode()).isEqualTo("DISCOUNT10");
        assertThat(checkout.getCheckoutState()).isEqualTo(CheckoutState.PENDING);
    }

    @Test
    void testCheckoutSetters_whenNormalCase_updateCheckout() {
        Checkout checkout = new Checkout();
        checkout.setId("checkout-456");
        checkout.setEmail("updated@example.com");
        checkout.setNote("Updated note");
        checkout.setPromotionCode("NEWDISCOUNT");
        checkout.setCheckoutState(CheckoutState.COMPLETED);

        assertThat(checkout.getId()).isEqualTo("checkout-456");
        assertThat(checkout.getEmail()).isEqualTo("updated@example.com");
        assertThat(checkout.getNote()).isEqualTo("Updated note");
        assertThat(checkout.getPromotionCode()).isEqualTo("NEWDISCOUNT");
        assertThat(checkout.getCheckoutState()).isEqualTo(CheckoutState.COMPLETED);
    }
}
