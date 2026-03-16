package com.yas.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.DuplicatedException;
import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.inventory.model.Warehouse;
import com.yas.inventory.model.enumeration.FilterExistInWhSelection;
import com.yas.inventory.repository.StockRepository;
import com.yas.inventory.repository.WarehouseRepository;
import com.yas.inventory.viewmodel.address.AddressDetailVm;
import com.yas.inventory.viewmodel.address.AddressPostVm;
import com.yas.inventory.viewmodel.address.AddressVm;
import com.yas.inventory.viewmodel.product.ProductInfoVm;
import com.yas.inventory.viewmodel.warehouse.WarehouseDetailVm;
import com.yas.inventory.viewmodel.warehouse.WarehouseGetVm;
import com.yas.inventory.viewmodel.warehouse.WarehouseListGetVm;
import com.yas.inventory.viewmodel.warehouse.WarehousePostVm;
import java.util.Collections;
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

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductService productService;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private WarehouseService warehouseService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = Warehouse.builder()
            .id(1L)
            .name("Test Warehouse")
            .addressId(10L)
            .build();
    }

    @Test
    void findAllWarehouses_shouldReturnListOfWarehouseGetVm() {
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

        List<WarehouseGetVm> result = warehouseService.findAllWarehouses();

        assertEquals(1, result.size());
        assertEquals("Test Warehouse", result.get(0).name());
    }

    @Test
    void getProductWarehouse_whenProductIdsNotEmpty_shouldReturnProductsWithExistFlag() {
        Long warehouseId = 1L;
        List<Long> productIds = List.of(100L, 200L);
        when(stockRepository.getProductIdsInWarehouse(warehouseId)).thenReturn(productIds);

        ProductInfoVm p1 = new ProductInfoVm(100L, "Product A", "SKU-A", false);
        ProductInfoVm p2 = new ProductInfoVm(300L, "Product C", "SKU-C", false);
        when(productService.filterProducts("name", "sku", productIds, FilterExistInWhSelection.YES))
            .thenReturn(List.of(p1, p2));

        List<ProductInfoVm> result = warehouseService.getProductWarehouse(
            warehouseId, "name", "sku", FilterExistInWhSelection.YES);

        assertEquals(2, result.size());
        assertEquals(true, result.get(0).existInWh());
        assertEquals(false, result.get(1).existInWh());
    }

    @Test
    void getProductWarehouse_whenProductIdsEmpty_shouldReturnProductsAsIs() {
        Long warehouseId = 1L;
        when(stockRepository.getProductIdsInWarehouse(warehouseId)).thenReturn(Collections.emptyList());

        ProductInfoVm p1 = new ProductInfoVm(100L, "Product A", "SKU-A", false);
        when(productService.filterProducts("name", "sku", Collections.emptyList(), FilterExistInWhSelection.ALL))
            .thenReturn(List.of(p1));

        List<ProductInfoVm> result = warehouseService.getProductWarehouse(
            warehouseId, "name", "sku", FilterExistInWhSelection.ALL);

        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).name());
    }

    @Test
    void findById_whenFound_shouldReturnWarehouseDetailVm() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        AddressDetailVm addressDetail = AddressDetailVm.builder()
            .id(10L)
            .contactName("John")
            .phone("123")
            .addressLine1("Line1")
            .addressLine2("Line2")
            .city("City")
            .zipCode("12345")
            .districtId(1L)
            .stateOrProvinceId(2L)
            .countryId(3L)
            .build();
        when(locationService.getAddressById(10L)).thenReturn(addressDetail);

        WarehouseDetailVm result = warehouseService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Test Warehouse", result.name());
        assertEquals("John", result.contactName());
    }

    @Test
    void findById_whenNotFound_shouldThrowNotFoundException() {
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> warehouseService.findById(99L));
    }

    @Test
    void create_whenNameNotDuplicated_shouldSaveAndReturn() {
        WarehousePostVm postVm = WarehousePostVm.builder()
            .name("New WH")
            .contactName("Contact")
            .phone("111")
            .addressLine1("Addr1")
            .addressLine2("Addr2")
            .city("City")
            .zipCode("00000")
            .districtId(1L)
            .stateOrProvinceId(2L)
            .countryId(3L)
            .build();

        when(warehouseRepository.existsByName("New WH")).thenReturn(false);
        AddressVm addressVm = AddressVm.builder().id(50L).build();
        when(locationService.createAddress(any(AddressPostVm.class))).thenReturn(addressVm);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse result = warehouseService.create(postVm);

        assertNotNull(result);
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    @Test
    void create_whenNameDuplicated_shouldThrowDuplicatedException() {
        WarehousePostVm postVm = WarehousePostVm.builder().name("Existing").build();
        when(warehouseRepository.existsByName("Existing")).thenReturn(true);

        assertThrows(DuplicatedException.class, () -> warehouseService.create(postVm));
    }

    @Test
    void update_whenValid_shouldUpdateSuccessfully() {
        WarehousePostVm postVm = WarehousePostVm.builder()
            .name("Updated WH")
            .contactName("C")
            .phone("222")
            .addressLine1("A1")
            .addressLine2("A2")
            .city("Ct")
            .zipCode("11111")
            .districtId(1L)
            .stateOrProvinceId(2L)
            .countryId(3L)
            .build();

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.existsByNameWithDifferentId("Updated WH", 1L)).thenReturn(false);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        warehouseService.update(postVm, 1L);

        verify(locationService).updateAddress(eq(10L), any(AddressPostVm.class));
        verify(warehouseRepository).save(warehouse);
    }

    @Test
    void update_whenNotFound_shouldThrowNotFoundException() {
        WarehousePostVm postVm = WarehousePostVm.builder().name("X").build();
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> warehouseService.update(postVm, 99L));
    }

    @Test
    void update_whenNameDuplicated_shouldThrowDuplicatedException() {
        WarehousePostVm postVm = WarehousePostVm.builder().name("Dup").build();
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.existsByNameWithDifferentId("Dup", 1L)).thenReturn(true);

        assertThrows(DuplicatedException.class, () -> warehouseService.update(postVm, 1L));
    }

    @Test
    void delete_whenFound_shouldDeleteWarehouseAndAddress() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        warehouseService.delete(1L);

        verify(warehouseRepository).deleteById(1L);
        verify(locationService).deleteAddress(10L);
    }

    @Test
    void delete_whenNotFound_shouldThrowNotFoundException() {
        when(warehouseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> warehouseService.delete(99L));
    }

    @Test
    void getPageableWarehouses_shouldReturnPagedResult() {
        List<Warehouse> warehouses = List.of(warehouse);
        Page<Warehouse> page = new PageImpl<>(warehouses, PageRequest.of(0, 10), 1);
        when(warehouseRepository.findAll(any(PageRequest.class))).thenReturn(page);

        WarehouseListGetVm result = warehouseService.getPageableWarehouses(0, 10);

        assertEquals(1, result.warehouseContent().size());
        assertEquals(0, result.pageNo());
        assertEquals(10, result.pageSize());
        assertEquals(1, result.totalElements());
    }
}
