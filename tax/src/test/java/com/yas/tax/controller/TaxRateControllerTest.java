package com.yas.tax.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.tax.model.TaxClass;
import com.yas.tax.model.TaxRate;
import com.yas.tax.service.TaxRateService;
import com.yas.tax.viewmodel.taxrate.TaxRateGetDetailVm;
import com.yas.tax.viewmodel.taxrate.TaxRateListGetVm;
import com.yas.tax.viewmodel.taxrate.TaxRatePostVm;
import com.yas.tax.viewmodel.taxrate.TaxRateVm;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class TaxRateControllerTest {

    @Mock
    private TaxRateService taxRateService;

    @InjectMocks
    private TaxRateController taxRateController;

    private TaxRate taxRate;
    private TaxRateVm taxRateVm;

    @BeforeEach
    void setUp() {
        TaxClass taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard");

        taxRate = new TaxRate();
        taxRate.setId(1L);
        taxRate.setRate(10.0);
        taxRate.setZipCode("12345");
        taxRate.setTaxClass(taxClass);
        taxRate.setStateOrProvinceId(100L);
        taxRate.setCountryId(200L);

        taxRateVm = new TaxRateVm(1L, 10.0, "12345", 1L, 100L, 200L);
    }

    @Test
    void getPageableTaxRates_shouldReturnOk() {
        TaxRateListGetVm listVm = new TaxRateListGetVm(
            List.of(new TaxRateGetDetailVm(1L, 10.0, "12345", "Standard", "California", "US")),
            0, 10, 1, 1, true);
        when(taxRateService.getPageableTaxRates(0, 10)).thenReturn(listVm);

        ResponseEntity<TaxRateListGetVm> response = taxRateController.getPageableTaxRates(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(listVm);
    }

    @Test
    void getTaxRate_shouldReturnOk() {
        when(taxRateService.findById(1L)).thenReturn(taxRateVm);

        ResponseEntity<TaxRateVm> response = taxRateController.getTaxRate(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taxRateVm);
    }

    @Test
    void createTaxRate_shouldReturnCreated() {
        TaxRatePostVm postVm = new TaxRatePostVm(10.0, "12345", 1L, 100L, 200L);
        when(taxRateService.createTaxRate(postVm)).thenReturn(taxRate);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<TaxRateVm> response = taxRateController.createTaxRate(postVm, uriBuilder);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isEqualTo(1L);
    }

    @Test
    void updateTaxRate_shouldReturnNoContent() {
        TaxRatePostVm postVm = new TaxRatePostVm(15.0, "54321", 1L, 101L, 201L);

        ResponseEntity<Void> response = taxRateController.updateTaxRate(1L, postVm);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taxRateService).updateTaxRate(postVm, 1L);
    }

    @Test
    void deleteTaxRate_shouldReturnNoContent() {
        ResponseEntity<Void> response = taxRateController.deleteTaxRate(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taxRateService).delete(1L);
    }

    @Test
    void getTaxPercentByAddress_shouldReturnOk() {
        when(taxRateService.getTaxPercent(1L, 200L, 100L, "12345")).thenReturn(10.0);

        ResponseEntity<Double> response = taxRateController.getTaxPercentByAddress(1L, 200L, 100L, "12345");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(10.0);
    }

    @Test
    void getBatchTaxPercentsByAddress_shouldReturnOk() {
        List<Long> taxClassIds = List.of(1L, 2L);
        when(taxRateService.getBulkTaxRate(taxClassIds, 200L, 100L, "12345"))
            .thenReturn(List.of(taxRateVm));

        ResponseEntity<List<TaxRateVm>> response =
            taxRateController.getBatchTaxPercentsByAddress(taxClassIds, 200L, 100L, "12345");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
}
