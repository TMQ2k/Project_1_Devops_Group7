package com.yas.delivery.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DeliveryServiceTest {

    @Test
    void shouldInstantiate() {
        DeliveryService service = new DeliveryService();
        assertThat(service).isNotNull();
    }
}
