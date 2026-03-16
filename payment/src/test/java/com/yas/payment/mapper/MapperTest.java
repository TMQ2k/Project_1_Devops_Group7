package com.yas.payment.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.payment.model.PaymentProvider;
import com.yas.payment.viewmodel.paymentprovider.CreatePaymentVm;
import com.yas.payment.viewmodel.paymentprovider.PaymentProviderVm;
import com.yas.payment.viewmodel.paymentprovider.UpdatePaymentVm;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MapperTest {

    private final PaymentProviderMapper paymentProviderMapper =
        Mappers.getMapper(PaymentProviderMapper.class);
    private final CreatePaymentProviderMapper createMapper =
        Mappers.getMapper(CreatePaymentProviderMapper.class);
    private final UpdatePaymentProviderMapper updateMapper =
        Mappers.getMapper(UpdatePaymentProviderMapper.class);

    // ==================== PaymentProviderMapper ====================

    @Test
    void paymentProviderMapper_toModel_withNull_shouldReturnNull() {
        assertThat(paymentProviderMapper.toModel(null)).isNull();
    }

    @Test
    void paymentProviderMapper_toModel_withValidInput() {
        var vm = new PaymentProviderVm("id1", "name1", "http://url", 1, 100L, "http://icon");
        var result = paymentProviderMapper.toModel(vm);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
        assertThat(result.getConfigureUrl()).isEqualTo("http://url");
        assertThat(result.getMediaId()).isEqualTo(100L);
    }

    @Test
    void paymentProviderMapper_toVm_withNull_shouldReturnNull() {
        assertThat(paymentProviderMapper.toVm(null)).isNull();
    }

    @Test
    void paymentProviderMapper_partialUpdate_withNonNullValues() {
        var model = createFullProvider();
        var vm = new PaymentProviderVm("new", "newName", "http://new", 2, 200L, "http://icon");
        paymentProviderMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("new");
        assertThat(model.getName()).isEqualTo("newName");
        assertThat(model.getConfigureUrl()).isEqualTo("http://new");
        assertThat(model.getMediaId()).isEqualTo(200L);
    }

    @Test
    void paymentProviderMapper_partialUpdate_withNullValues_shouldIgnore() {
        var model = createFullProvider();
        var vm = new PaymentProviderVm(null, null, null, 0, null, null);
        paymentProviderMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("id1");
        assertThat(model.getName()).isEqualTo("name1");
        assertThat(model.getConfigureUrl()).isEqualTo("http://url");
        assertThat(model.getMediaId()).isEqualTo(100L);
    }

    @Test
    void paymentProviderMapper_partialUpdate_withNullSource() {
        var model = createFullProvider();
        paymentProviderMapper.partialUpdate(model, null);
        assertThat(model.getId()).isEqualTo("id1");
    }

    // ==================== CreatePaymentProviderMapper ====================

    @Test
    void createMapper_toModel_withNull_shouldReturnNull() {
        assertThat(createMapper.toModel(null)).isNull();
    }

    @Test
    void createMapper_toModel_shouldSetIsNewTrue() {
        var vm = createFullCreateVm();
        var result = createMapper.toModel(vm);
        assertThat(result).isNotNull();
        assertThat(result.isNew()).isTrue();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.getConfigureUrl()).isEqualTo("http://url");
        assertThat(result.getLandingViewComponentName()).isEqualTo("component");
        assertThat(result.getAdditionalSettings()).isEqualTo("settings");
        assertThat(result.getMediaId()).isEqualTo(100L);
    }

    @Test
    void createMapper_toVm_withNull_shouldReturnNull() {
        assertThat(createMapper.toVm(null)).isNull();
    }

    @Test
    void createMapper_toVm_withValidInput() {
        var provider = createFullProvider();
        var result = createMapper.toVm(provider);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
    }

    @Test
    void createMapper_toVmResponse_withNull_shouldReturnNull() {
        assertThat(createMapper.toVmResponse(null)).isNull();
    }

    @Test
    void createMapper_toVmResponse_withValidInput() {
        var provider = createFullProvider();
        var result = createMapper.toVmResponse(provider);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
        assertThat(result.getConfigureUrl()).isEqualTo("http://url");
        assertThat(result.getMediaId()).isEqualTo(100L);
    }

    @Test
    void createMapper_partialUpdate_withNonNullValues() {
        var model = new PaymentProvider();
        model.setId("old");
        var vm = createFullCreateVm();
        createMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("id1");
        assertThat(model.getName()).isEqualTo("name1");
        assertThat(model.getConfigureUrl()).isEqualTo("http://url");
        assertThat(model.getLandingViewComponentName()).isEqualTo("component");
        assertThat(model.getAdditionalSettings()).isEqualTo("settings");
        assertThat(model.getMediaId()).isEqualTo(100L);
    }

    @Test
    void createMapper_partialUpdate_withNullValues_shouldIgnore() {
        var model = createFullProvider();
        var vm = new CreatePaymentVm();
        createMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("id1");
        assertThat(model.getName()).isEqualTo("name1");
        assertThat(model.getConfigureUrl()).isEqualTo("http://url");
        assertThat(model.getLandingViewComponentName()).isEqualTo("component");
        assertThat(model.getAdditionalSettings()).isEqualTo("settings");
        assertThat(model.getMediaId()).isEqualTo(100L);
    }

    @Test
    void createMapper_partialUpdate_withNullSource() {
        var model = createFullProvider();
        createMapper.partialUpdate(model, null);
        assertThat(model.getId()).isEqualTo("id1");
    }

    // ==================== UpdatePaymentProviderMapper ====================

    @Test
    void updateMapper_toModel_withNull_shouldReturnNull() {
        assertThat(updateMapper.toModel(null)).isNull();
    }

    @Test
    void updateMapper_toModel_withValidInput() {
        var vm = createFullUpdateVm();
        var result = updateMapper.toModel(vm);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
        assertThat(result.isEnabled()).isTrue();
        assertThat(result.getConfigureUrl()).isEqualTo("http://url");
        assertThat(result.getLandingViewComponentName()).isEqualTo("component");
        assertThat(result.getAdditionalSettings()).isEqualTo("settings");
        assertThat(result.getMediaId()).isEqualTo(100L);
    }

    @Test
    void updateMapper_toVm_withNull_shouldReturnNull() {
        assertThat(updateMapper.toVm(null)).isNull();
    }

    @Test
    void updateMapper_toVm_withValidInput() {
        var provider = createFullProvider();
        var result = updateMapper.toVm(provider);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
    }

    @Test
    void updateMapper_toVmResponse_withNull_shouldReturnNull() {
        assertThat(updateMapper.toVmResponse(null)).isNull();
    }

    @Test
    void updateMapper_toVmResponse_withValidInput() {
        var provider = createFullProvider();
        var result = updateMapper.toVmResponse(provider);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getName()).isEqualTo("name1");
    }

    @Test
    void updateMapper_partialUpdate_withNonNullValues() {
        var model = new PaymentProvider();
        model.setId("old");
        var vm = createFullUpdateVm();
        updateMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("id1");
        assertThat(model.getName()).isEqualTo("name1");
        assertThat(model.getConfigureUrl()).isEqualTo("http://url");
        assertThat(model.getLandingViewComponentName()).isEqualTo("component");
        assertThat(model.getAdditionalSettings()).isEqualTo("settings");
        assertThat(model.getMediaId()).isEqualTo(100L);
    }

    @Test
    void updateMapper_partialUpdate_withNullValues_shouldIgnore() {
        var model = createFullProvider();
        var vm = new UpdatePaymentVm();
        updateMapper.partialUpdate(model, vm);
        assertThat(model.getId()).isEqualTo("id1");
        assertThat(model.getName()).isEqualTo("name1");
        assertThat(model.getConfigureUrl()).isEqualTo("http://url");
        assertThat(model.getLandingViewComponentName()).isEqualTo("component");
        assertThat(model.getAdditionalSettings()).isEqualTo("settings");
        assertThat(model.getMediaId()).isEqualTo(100L);
    }

    @Test
    void updateMapper_partialUpdate_withNullSource() {
        var model = createFullProvider();
        updateMapper.partialUpdate(model, null);
        assertThat(model.getId()).isEqualTo("id1");
    }

    // ==================== Helpers ====================

    private PaymentProvider createFullProvider() {
        var provider = new PaymentProvider();
        provider.setId("id1");
        provider.setName("name1");
        provider.setEnabled(true);
        provider.setConfigureUrl("http://url");
        provider.setLandingViewComponentName("component");
        provider.setAdditionalSettings("settings");
        provider.setMediaId(100L);
        return provider;
    }

    private CreatePaymentVm createFullCreateVm() {
        var vm = new CreatePaymentVm();
        vm.setId("id1");
        vm.setName("name1");
        vm.setEnabled(true);
        vm.setConfigureUrl("http://url");
        vm.setLandingViewComponentName("component");
        vm.setAdditionalSettings("settings");
        vm.setMediaId(100L);
        return vm;
    }

    private UpdatePaymentVm createFullUpdateVm() {
        var vm = new UpdatePaymentVm();
        vm.setId("id1");
        vm.setName("name1");
        vm.setEnabled(true);
        vm.setConfigureUrl("http://url");
        vm.setLandingViewComponentName("component");
        vm.setAdditionalSettings("settings");
        vm.setMediaId(100L);
        return vm;
    }
}
