package com.yas.tax.viewmodel;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.tax.model.TaxClass;
import com.yas.tax.model.TaxRate;
import com.yas.tax.viewmodel.error.ErrorVm;
import com.yas.tax.viewmodel.location.StateOrProvinceAndCountryGetNameVm;
import com.yas.tax.viewmodel.taxclass.TaxClassListGetVm;
import com.yas.tax.viewmodel.taxclass.TaxClassPostVm;
import com.yas.tax.viewmodel.taxclass.TaxClassVm;
import com.yas.tax.viewmodel.taxrate.TaxRateGetDetailVm;
import com.yas.tax.viewmodel.taxrate.TaxRateListGetVm;
import com.yas.tax.viewmodel.taxrate.TaxRatePostVm;
import com.yas.tax.viewmodel.taxrate.TaxRateVm;
import java.util.List;
import org.junit.jupiter.api.Test;

class ViewModelTest {

    @Test
    void taxClassVm_fromModel_shouldMapCorrectly() {
        TaxClass taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard");

        TaxClassVm vm = TaxClassVm.fromModel(taxClass);

        assertThat(vm.id()).isEqualTo(1L);
        assertThat(vm.name()).isEqualTo("Standard");
    }

    @Test
    void taxClassPostVm_toModel_shouldMapCorrectly() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "New Tax");

        TaxClass model = postVm.toModel();

        assertThat(model.getName()).isEqualTo("New Tax");
        assertThat(postVm.id()).isEqualTo("id1");
        assertThat(postVm.name()).isEqualTo("New Tax");
    }

    @Test
    void taxClassListGetVm_shouldHoldData() {
        TaxClassListGetVm vm = new TaxClassListGetVm(
            List.of(new TaxClassVm(1L, "Standard")), 0, 10, 1, 1, true);

        assertThat(vm.taxClassContent()).hasSize(1);
        assertThat(vm.pageNo()).isZero();
        assertThat(vm.pageSize()).isEqualTo(10);
        assertThat(vm.totalElements()).isEqualTo(1);
        assertThat(vm.totalPages()).isEqualTo(1);
        assertThat(vm.isLast()).isTrue();
    }

    @Test
    void taxRateVm_fromModel_shouldMapCorrectly() {
        TaxClass taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard");

        TaxRate taxRate = new TaxRate();
        taxRate.setId(1L);
        taxRate.setRate(10.0);
        taxRate.setZipCode("12345");
        taxRate.setTaxClass(taxClass);
        taxRate.setStateOrProvinceId(100L);
        taxRate.setCountryId(200L);

        TaxRateVm vm = TaxRateVm.fromModel(taxRate);

        assertThat(vm.id()).isEqualTo(1L);
        assertThat(vm.rate()).isEqualTo(10.0);
        assertThat(vm.zipCode()).isEqualTo("12345");
        assertThat(vm.taxClassId()).isEqualTo(1L);
        assertThat(vm.stateOrProvinceId()).isEqualTo(100L);
        assertThat(vm.countryId()).isEqualTo(200L);
    }

    @Test
    void taxRatePostVm_shouldHoldData() {
        TaxRatePostVm postVm = new TaxRatePostVm(10.0, "12345", 1L, 100L, 200L);

        assertThat(postVm.rate()).isEqualTo(10.0);
        assertThat(postVm.zipCode()).isEqualTo("12345");
        assertThat(postVm.taxClassId()).isEqualTo(1L);
        assertThat(postVm.stateOrProvinceId()).isEqualTo(100L);
        assertThat(postVm.countryId()).isEqualTo(200L);
    }

    @Test
    void taxRateGetDetailVm_shouldHoldData() {
        TaxRateGetDetailVm vm = new TaxRateGetDetailVm(1L, 10.0, "12345", "Standard", "California", "US");

        assertThat(vm.id()).isEqualTo(1L);
        assertThat(vm.rate()).isEqualTo(10.0);
        assertThat(vm.zipCode()).isEqualTo("12345");
        assertThat(vm.taxClassName()).isEqualTo("Standard");
        assertThat(vm.stateOrProvinceName()).isEqualTo("California");
        assertThat(vm.countryName()).isEqualTo("US");
    }

    @Test
    void taxRateListGetVm_shouldHoldData() {
        TaxRateListGetVm vm = new TaxRateListGetVm(
            List.of(new TaxRateGetDetailVm(1L, 10.0, "12345", "Standard", "CA", "US")),
            0, 10, 1, 1, true);

        assertThat(vm.taxRateGetDetailContent()).hasSize(1);
        assertThat(vm.pageNo()).isZero();
        assertThat(vm.pageSize()).isEqualTo(10);
        assertThat(vm.totalElements()).isEqualTo(1);
        assertThat(vm.totalPages()).isEqualTo(1);
        assertThat(vm.isLast()).isTrue();
    }

    @Test
    void stateOrProvinceAndCountryGetNameVm_shouldHoldData() {
        StateOrProvinceAndCountryGetNameVm vm =
            new StateOrProvinceAndCountryGetNameVm(100L, "California", "United States");

        assertThat(vm.stateOrProvinceId()).isEqualTo(100L);
        assertThat(vm.stateOrProvinceName()).isEqualTo("California");
        assertThat(vm.countryName()).isEqualTo("United States");
    }

    @Test
    void errorVm_withFieldErrors_shouldHoldData() {
        ErrorVm vm = new ErrorVm("404", "Not Found", "Tax class not found", List.of("field1"));

        assertThat(vm.statusCode()).isEqualTo("404");
        assertThat(vm.title()).isEqualTo("Not Found");
        assertThat(vm.detail()).isEqualTo("Tax class not found");
        assertThat(vm.fieldErrors()).containsExactly("field1");
    }

    @Test
    void errorVm_withoutFieldErrors_shouldUseEmptyList() {
        ErrorVm vm = new ErrorVm("400", "Bad Request", "Invalid input");

        assertThat(vm.statusCode()).isEqualTo("400");
        assertThat(vm.title()).isEqualTo("Bad Request");
        assertThat(vm.detail()).isEqualTo("Invalid input");
        assertThat(vm.fieldErrors()).isEmpty();
    }
}
