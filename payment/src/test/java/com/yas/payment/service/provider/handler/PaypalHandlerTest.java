package com.yas.payment.service.provider.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.yas.payment.model.enumeration.PaymentMethod;
import com.yas.payment.model.enumeration.PaymentStatus;
import com.yas.payment.paypal.service.PaypalService;
import com.yas.payment.paypal.viewmodel.PaypalCapturePaymentResponse;
import com.yas.payment.paypal.viewmodel.PaypalCreatePaymentResponse;
import com.yas.payment.service.PaymentProviderService;
import com.yas.payment.viewmodel.CapturePaymentRequestVm;
import com.yas.payment.viewmodel.InitPaymentRequestVm;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaypalHandlerTest {

    private PaypalService paypalService;
    private PaymentProviderService paymentProviderService;
    private PaypalHandler paypalHandler;

    @BeforeEach
    void setUp() {
        paypalService = mock(PaypalService.class);
        paymentProviderService = mock(PaymentProviderService.class);
        paypalHandler = new PaypalHandler(paymentProviderService, paypalService);
        when(paymentProviderService.getAdditionalSettingsByPaymentProviderId("PAYPAL"))
            .thenReturn("{\"clientId\":\"test\"}");
    }

    @Test
    void getProviderId_shouldReturnPaypal() {
        assertThat(paypalHandler.getProviderId()).isEqualTo(PaymentMethod.PAYPAL.name());
    }

    @Test
    void initPayment_shouldReturnInitiatedPayment() {
        var request = InitPaymentRequestVm.builder()
            .paymentMethod("PAYPAL")
            .totalPrice(BigDecimal.TEN)
            .checkoutId("checkout-123")
            .build();
        var paypalResponse = PaypalCreatePaymentResponse.builder()
            .status("CREATED")
            .paymentId("PAY-123")
            .redirectUrl("http://paypal.com/approve")
            .build();
        when(paypalService.createPayment(any())).thenReturn(paypalResponse);

        var result = paypalHandler.initPayment(request);

        assertThat(result.getStatus()).isEqualTo("CREATED");
        assertThat(result.getPaymentId()).isEqualTo("PAY-123");
        assertThat(result.getRedirectUrl()).isEqualTo("http://paypal.com/approve");
    }

    @Test
    void capturePayment_shouldReturnCapturedPayment() {
        var request = CapturePaymentRequestVm.builder()
            .paymentMethod("PAYPAL")
            .token("TOKEN-123")
            .build();
        var paypalResponse = PaypalCapturePaymentResponse.builder()
            .checkoutId("checkout-123")
            .amount(BigDecimal.valueOf(100))
            .paymentFee(BigDecimal.ZERO)
            .gatewayTransactionId("TXN-123")
            .paymentMethod("PAYPAL")
            .paymentStatus("COMPLETED")
            .failureMessage(null)
            .build();
        when(paypalService.capturePayment(any())).thenReturn(paypalResponse);

        var result = paypalHandler.capturePayment(request);

        assertThat(result.getCheckoutId()).isEqualTo("checkout-123");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.getGatewayTransactionId()).isEqualTo("TXN-123");
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.PAYPAL);
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }
}
