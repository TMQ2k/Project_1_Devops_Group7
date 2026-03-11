package com.yas.payment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.yas.payment.service.PaymentService;
import com.yas.payment.viewmodel.CapturePaymentRequestVm;
import com.yas.payment.viewmodel.CapturePaymentResponseVm;
import com.yas.payment.viewmodel.InitPaymentRequestVm;
import com.yas.payment.viewmodel.InitPaymentResponseVm;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PaymentControllerTest {

    private PaymentService paymentService;
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void initPayment_shouldDelegateToService() {
        var request = InitPaymentRequestVm.builder()
            .paymentMethod("PAYPAL").totalPrice(BigDecimal.TEN).checkoutId("c1").build();
        var expected = InitPaymentResponseVm.builder()
            .status("CREATED").paymentId("P1").redirectUrl("http://url").build();
        when(paymentService.initPayment(request)).thenReturn(expected);

        var result = paymentController.initPayment(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void capturePayment_shouldDelegateToService() {
        var request = CapturePaymentRequestVm.builder()
            .paymentMethod("PAYPAL").token("T1").build();
        var expected = CapturePaymentResponseVm.builder().orderId(1L).build();
        when(paymentService.capturePayment(request)).thenReturn(expected);

        var result = paymentController.capturePayment(request);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void cancelPayment_shouldReturnOk() {
        var result = paymentController.cancelPayment();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Payment cancelled");
    }
}
