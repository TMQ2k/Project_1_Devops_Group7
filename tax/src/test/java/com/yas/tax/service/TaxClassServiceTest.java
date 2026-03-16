package com.yas.tax.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.DuplicatedException;
import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.tax.model.TaxClass;
import com.yas.tax.repository.TaxClassRepository;
import com.yas.tax.viewmodel.taxclass.TaxClassListGetVm;
import com.yas.tax.viewmodel.taxclass.TaxClassPostVm;
import com.yas.tax.viewmodel.taxclass.TaxClassVm;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class TaxClassServiceTest {

    @Mock
    private TaxClassRepository taxClassRepository;

    @InjectMocks
    private TaxClassService taxClassService;

    private TaxClass taxClass;

    @BeforeEach
    void setUp() {
        taxClass = new TaxClass();
        taxClass.setId(1L);
        taxClass.setName("Standard Tax");
    }

    @Test
    void findAllTaxClasses_shouldReturnList() {
        when(taxClassRepository.findAll(Sort.by(Sort.Direction.ASC, "name")))
            .thenReturn(List.of(taxClass));

        List<TaxClassVm> result = taxClassService.findAllTaxClasses();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).name()).isEqualTo("Standard Tax");
    }

    @Test
    void findById_whenExists_shouldReturnTaxClassVm() {
        when(taxClassRepository.findById(1L)).thenReturn(Optional.of(taxClass));

        TaxClassVm result = taxClassService.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Standard Tax");
    }

    @Test
    void findById_whenNotExists_shouldThrowNotFoundException() {
        when(taxClassRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taxClassService.findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_whenNameNotDuplicated_shouldSave() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "New Tax");
        when(taxClassRepository.existsByName("New Tax")).thenReturn(false);
        when(taxClassRepository.save(any(TaxClass.class))).thenReturn(taxClass);

        TaxClass result = taxClassService.create(postVm);

        assertThat(result).isNotNull();
        verify(taxClassRepository).save(any(TaxClass.class));
    }

    @Test
    void create_whenNameDuplicated_shouldThrowDuplicatedException() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Standard Tax");
        when(taxClassRepository.existsByName("Standard Tax")).thenReturn(true);

        assertThatThrownBy(() -> taxClassService.create(postVm))
            .isInstanceOf(DuplicatedException.class);
    }

    @Test
    void update_whenExistsAndNameNotDuplicated_shouldUpdate() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Updated Tax");
        when(taxClassRepository.findById(1L)).thenReturn(Optional.of(taxClass));
        when(taxClassRepository.existsByNameNotUpdatingTaxClass("Updated Tax", 1L)).thenReturn(false);

        taxClassService.update(postVm, 1L);

        assertThat(taxClass.getName()).isEqualTo("Updated Tax");
        verify(taxClassRepository).save(taxClass);
    }

    @Test
    void update_whenNotExists_shouldThrowNotFoundException() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Updated Tax");
        when(taxClassRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taxClassService.update(postVm, 99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_whenNameDuplicated_shouldThrowDuplicatedException() {
        TaxClassPostVm postVm = new TaxClassPostVm("id1", "Duplicate Name");
        when(taxClassRepository.findById(1L)).thenReturn(Optional.of(taxClass));
        when(taxClassRepository.existsByNameNotUpdatingTaxClass("Duplicate Name", 1L)).thenReturn(true);

        assertThatThrownBy(() -> taxClassService.update(postVm, 1L))
            .isInstanceOf(DuplicatedException.class);
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(taxClassRepository.existsById(1L)).thenReturn(true);

        taxClassService.delete(1L);

        verify(taxClassRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotExists_shouldThrowNotFoundException() {
        when(taxClassRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> taxClassService.delete(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getPageableTaxClasses_shouldReturnPagedResult() {
        List<TaxClass> taxClasses = List.of(taxClass);
        Page<TaxClass> page = new PageImpl<>(taxClasses, PageRequest.of(0, 10), 1);
        when(taxClassRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        TaxClassListGetVm result = taxClassService.getPageableTaxClasses(0, 10);

        assertThat(result.taxClassContent()).hasSize(1);
        assertThat(result.pageNo()).isZero();
        assertThat(result.pageSize()).isEqualTo(10);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.isLast()).isTrue();
    }
}
