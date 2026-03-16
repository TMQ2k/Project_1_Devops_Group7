package com.yas.delivery.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DeliveryControllerTest {

    @Test
    void shouldInstantiate() {
        DeliveryController controller = new DeliveryController();
        assertThat(controller).isNotNull();
    }
}
