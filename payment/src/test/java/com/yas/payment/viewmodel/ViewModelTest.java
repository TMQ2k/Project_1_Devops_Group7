package com.yas.payment.viewmodel;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.payment.model.PaymentProvider;
import java.util.List;
import org.junit.jupiter.api.Test;

class ViewModelTest {

    @Test
    void errorVm_threeArgConstructor_shouldCreateWithEmptyFieldErrors() {
        var errorVm = new ErrorVm("400", "Bad Request", "Invalid input");
        assertThat(errorVm.statusCode()).isEqualTo("400");
        assertThat(errorVm.title()).isEqualTo("Bad Request");
        assertThat(errorVm.detail()).isEqualTo("Invalid input");
        assertThat(errorVm.fieldErrors()).isEmpty();
    }

    @Test
    void errorVm_fourArgConstructor_shouldCreateWithFieldErrors() {
        var errors = List.of("field1", "field2");
        var errorVm = new ErrorVm("400", "Bad Request", "Invalid input", errors);
        assertThat(errorVm.fieldErrors()).hasSize(2);
    }

    @Test
    void paymentProvider_isNew_shouldReturnFlag() {
        var provider = new PaymentProvider();
        provider.setNew(true);
        assertThat(provider.isNew()).isTrue();

        provider.setNew(false);
        assertThat(provider.isNew()).isFalse();
    }

    @Test
    void checkoutStatusVm_shouldHoldValues() {
        var vm = new CheckoutStatusVm("checkout-1", "COMPLETED");
        assertThat(vm.checkoutId()).isEqualTo("checkout-1");
        assertThat(vm.checkoutStatus()).isEqualTo("COMPLETED");
    }
}
