package com.yas.tax.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.tax.model.TaxClass;
import com.yas.tax.service.TaxClassService;
import com.yas.tax.viewmodel.taxclass.TaxClassListGetVm;
import com.yas.tax.viewmodel.taxclass.TaxClassPostVm;
import com.yas.tax.viewmodel.taxclass.TaxClassVm;
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
class TaxClassControllerTest {

    @Mock
    private TaxClassService taxClassService;

    @InjectMocks
    private TaxClassController taxClassController;

    private TaxClass taxClass;
    private TaxClassVm taxClassVm;

    @BeforeEach
    void setUp() {
        taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard Tax");
        taxClassVm = new TaxClassVm(1L, "Standard Tax");
    }

    @Test
    void getPageableTaxClasses_shouldReturnOk() {
        TaxClassListGetVm listVm = new TaxClassListGetVm(List.of(taxClassVm), 0, 10, 1, 1, true);
        when(taxClassService.getPageableTaxClasses(0, 10)).thenReturn(listVm);

        ResponseEntity<TaxClassListGetVm> response = taxClassController.getPageableTaxClasses(0, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(listVm);
    }

    @Test
    void listTaxClasses_shouldReturnOk() {
        when(taxClassService.findAllTaxClasses()).thenReturn(List.of(taxClassVm));

        ResponseEntity<List<TaxClassVm>> response = taxClassController.listTaxClasses();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    void getTaxClass_shouldReturnOk() {
        when(taxClassService.findById(1L)).thenReturn(taxClassVm);

        ResponseEntity<TaxClassVm> response = taxClassController.getTaxClass(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(taxClassVm);
    }

    @Test
    void createTaxClass_shouldReturnCreated() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Standard Tax");
        when(taxClassService.create(postVm)).thenReturn(taxClass);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

        ResponseEntity<TaxClassVm> response = taxClassController.createTaxClass(postVm, uriBuilder);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isEqualTo(1L);
    }

    @Test
    void updateTaxClass_shouldReturnNoContent() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Updated Tax");

        ResponseEntity<Void> response = taxClassController.updateTaxClass(1L, postVm);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taxClassService).update(postVm, 1L);
    }

    @Test
    void deleteTaxClass_shouldReturnNoContent() {
        ResponseEntity<Void> response = taxClassController.deleteTaxClass(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taxClassService).delete(1L);
    }
}
