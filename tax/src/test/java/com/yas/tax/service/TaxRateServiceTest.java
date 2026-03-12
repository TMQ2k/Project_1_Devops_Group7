package com.yas.tax.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.tax.model.TaxClass;
import com.yas.tax.model.TaxRate;
import com.yas.tax.repository.TaxClassRepository;
import com.yas.tax.repository.TaxRateRepository;
import com.yas.tax.viewmodel.location.StateOrProvinceAndCountryGetNameVm;
import com.yas.tax.viewmodel.taxrate.TaxRateListGetVm;
import com.yas.tax.viewmodel.taxrate.TaxRatePostVm;
import com.yas.tax.viewmodel.taxrate.TaxRateVm;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TaxRateServiceTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    @Mock
    private TaxClassRepository taxClassRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private TaxRateService taxRateService;

    private TaxClass taxClass;
    private TaxRate taxRate;

    @BeforeEach
    void setUp() {
        taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard");

        taxRate = new TaxRate();
        taxRate.setId(1L);
        taxRate.setRate(10.0);
        taxRate.setZipCode("12345");
        taxRate.setTaxClass(taxClass);
        taxRate.setStateOrProvinceId(100L);
        taxRate.setCountryId(200L);
    }

    @Test
    void createTaxRate_whenTaxClassExists_shouldCreate() {
        TaxRatePostVm postVm = new TaxRatePostVm(10.0, "12345", 1L, 100L, 200L);
        when(taxClassRepository.existsById(1L)).thenReturn(true);
        when(taxClassRepository.getReferenceById(1L)).thenReturn(taxClass);
        when(taxRateRepository.save(any(TaxRate.class))).thenReturn(taxRate);

        TaxRate result = taxRateService.createTaxRate(postVm);

        assertThat(result).isNotNull();
        assertThat(result.getRate()).isEqualTo(10.0);
    }

    @Test
    void createTaxRate_whenTaxClassNotExists_shouldThrowNotFoundException() {
        TaxRatePostVm postVm = new TaxRatePostVm(10.0, "12345", 99L, 100L, 200L);
        when(taxClassRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taxRateService.createTaxRate(postVm))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateTaxRate_whenBothExist_shouldUpdate() {
        TaxRatePostVm postVm = new TaxRatePostVm(15.0, "54321", 1L, 101L, 201L);
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));
        when(taxClassRepository.existsById(1L)).thenReturn(true);
        when(taxClassRepository.getReferenceById(1L)).thenReturn(taxClass);

        taxRateService.updateTaxRate(postVm, 1L);

        assertThat(taxRate.getRate()).isEqualTo(15.0);
        assertThat(taxRate.getZipCode()).isEqualTo("54321");
        verify(taxRateRepository).save(taxRate);
    }

    @Test
    void updateTaxRate_whenTaxRateNotExists_shouldThrowNotFoundException() {
        TaxRatePostVm postVm = new TaxRatePostVm(15.0, "54321", 1L, 101L, 201L);
        when(taxRateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taxRateService.updateTaxRate(postVm, 99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateTaxRate_whenTaxClassNotExists_shouldThrowNotFoundException() {
        TaxRatePostVm postVm = new TaxRatePostVm(15.0, "54321", 99L, 101L, 201L);
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));
        when(taxClassRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taxRateService.updateTaxRate(postVm, 1L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(taxRateRepository.existsById(1L)).thenReturn(true);

        taxRateService.delete(1L);

        verify(taxRateRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotExists_shouldThrowNotFoundException() {
        when(taxRateRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taxRateService.delete(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findById_whenExists_shouldReturnVm() {
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));

        TaxRateVm result = taxRateService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.rate()).isEqualTo(10.0);
    }

    @Test
    void findById_whenNotExists_shouldThrowNotFoundException() {
        when(taxRateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taxRateService.findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAll_shouldReturnAllTaxRates() {
        when(taxRateRepository.findAll()).thenReturn(List.of(taxRate));

        List<TaxRateVm> result = taxRateService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }

    @Test
    void getPageableTaxRates_withNonEmptyStateOrProvinceIds_shouldReturnPagedResult() {
        List<TaxRate> taxRates = List.of(taxRate);
        Page<TaxRate> page = new PageImpl<>(taxRates, PageRequest.of(0, 10), 1);
        when(taxRateRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        StateOrProvinceAndCountryGetNameVm locationVm =
            new StateOrProvinceAndCountryGetNameVm(100L, "California", "United States");
        when(locationService.getStateOrProvinceAndCountryNames(List.of(100L)))
            .thenReturn(List.of(locationVm));

        TaxRateListGetVm result = taxRateService.getPageableTaxRates(0, 10);

        assertThat(result.taxRateGetDetailContent()).hasSize(1);
        assertThat(result.pageNo()).isZero();
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }

    @Test
    void getPageableTaxRates_withEmptyList_shouldReturnEmptyPagedResult() {
        Page<TaxRate> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(taxRateRepository.findAll(PageRequest.of(0, 10))).thenReturn(emptyPage);

        TaxRateListGetVm result = taxRateService.getPageableTaxRates(0, 10);

        assertThat(result.taxRateGetDetailContent()).isEmpty();
    }

    @Test
    void getTaxPercent_whenPercentExists_shouldReturnPercent() {
        when(taxRateRepository.getTaxPercent(200L, 100L, "12345", 1L)).thenReturn(10.0);

        double result = taxRateService.getTaxPercent(1L, 200L, 100L, "12345");

        assertThat(result).isEqualTo(10.0);
    }

    @Test
    void getTaxPercent_whenPercentNull_shouldReturnZero() {
        when(taxRateRepository.getTaxPercent(200L, 100L, "12345", 1L)).thenReturn(null);

        double result = taxRateService.getTaxPercent(1L, 200L, 100L, "12345");

        assertThat(result).isZero();
    }

    @Test
    void getBulkTaxRate_shouldReturnTaxRates() {
        List<Long> taxClassIds = List.of(1L, 2L);
        when(taxRateRepository.getBatchTaxRates(200L, 100L, "12345", new HashSet<>(taxClassIds)))
            .thenReturn(List.of(taxRate));

        List<TaxRateVm> result = taxRateService.getBulkTaxRate(taxClassIds, 200L, 100L, "12345");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }
}
