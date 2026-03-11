package com.yas.payment.paypal.service;

import com.paypal.core.PayPalHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PayPalHttpClientInitializerTest {

    private PayPalHttpClientInitializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new PayPalHttpClientInitializer();
    }

    @Test
    void createPaypalClient_whenSandboxMode_returnsSandboxClient() {
        String settings = "{\"clientId\": \"testId\", \"clientSecret\": \"testSecret\", \"mode\": \"sandbox\"}";
        PayPalHttpClient client = initializer.createPaypalClient(settings);
        assertNotNull(client);
    }

    @Test
    void createPaypalClient_whenLiveMode_returnsLiveClient() {
        String settings = "{\"clientId\": \"testId\", \"clientSecret\": \"testSecret\", \"mode\": \"live\"}";
        PayPalHttpClient client = initializer.createPaypalClient(settings);
        assertNotNull(client);
    }

    @Test
    void createPaypalClient_whenNullSettings_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> initializer.createPaypalClient(null));
    }
}
