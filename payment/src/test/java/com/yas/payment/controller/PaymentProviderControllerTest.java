package com.yas.payment.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.yas.payment.service.PaymentProviderService;
import com.yas.payment.viewmodel.paymentprovider.CreatePaymentVm;
import com.yas.payment.viewmodel.paymentprovider.PaymentProviderVm;
import com.yas.payment.viewmodel.paymentprovider.UpdatePaymentVm;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

class PaymentProviderControllerTest {

    private PaymentProviderService paymentProviderService;
    private PaymentProviderController controller;

    @BeforeEach
    void setUp() {
        paymentProviderService = mock(PaymentProviderService.class);
        controller = new PaymentProviderController(paymentProviderService);
    }

    @Test
    void create_shouldReturnCreated() {
        var request = new CreatePaymentVm();
        request.setId("id");
        request.setName("name");
        var expected = new PaymentProviderVm("id", "name", "url", 1, null, null);
        when(paymentProviderService.create(request)).thenReturn(expected);

        var result = controller.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void update_shouldReturnOk() {
        var request = new UpdatePaymentVm();
        request.setId("id");
        var expected = new PaymentProviderVm("id", "name", "url", 1, null, null);
        when(paymentProviderService.update(request)).thenReturn(expected);

        var result = controller.update(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void getAll_shouldReturnList() {
        var pageable = Pageable.ofSize(10);
        var expected = List.of(new PaymentProviderVm("id", "name", "url", 1, null, null));
        when(paymentProviderService.getEnabledPaymentProviders(pageable)).thenReturn(expected);

        var result = controller.getAll(pageable);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
    }
}
