package com.yas.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.BadRequestException;
import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.commonlibrary.exception.StockExistingException;
import com.yas.inventory.model.Stock;
import com.yas.inventory.model.Warehouse;
import com.yas.inventory.model.enumeration.FilterExistInWhSelection;
import com.yas.inventory.repository.StockRepository;
import com.yas.inventory.repository.WarehouseRepository;
import com.yas.inventory.viewmodel.product.ProductInfoVm;
import com.yas.inventory.viewmodel.stock.StockPostVm;
import com.yas.inventory.viewmodel.stock.StockQuantityUpdateVm;
import com.yas.inventory.viewmodel.stock.StockQuantityVm;
import com.yas.inventory.viewmodel.stock.StockVm;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductService productService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private StockHistoryService stockHistoryService;

    private StockService stockService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        stockService = new StockService(
            warehouseRepository, stockRepository, productService, warehouseService, stockHistoryService);
        warehouse = Warehouse.builder().id(1L).name("WH1").addressId(10L).build();
    }

    @Test
    void addProductIntoWarehouse_whenValid_shouldSaveStocks() {
        StockPostVm postVm = new StockPostVm(100L, 1L);

        when(stockRepository.existsByWarehouseIdAndProductId(1L, 100L)).thenReturn(false);
        when(productService.getProduct(100L))
            .thenReturn(new ProductInfoVm(100L, "Product", "SKU", false));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        stockService.addProductIntoWarehouse(List.of(postVm));

        verify(stockRepository).saveAll(anyList());
    }

    @Test
    void addProductIntoWarehouse_whenStockAlreadyExists_shouldThrow() {
        StockPostVm postVm = new StockPostVm(100L, 1L);
        when(stockRepository.existsByWarehouseIdAndProductId(1L, 100L)).thenReturn(true);

        assertThrows(StockExistingException.class,
            () -> stockService.addProductIntoWarehouse(List.of(postVm)));
    }

    @Test
    void addProductIntoWarehouse_whenProductNotFound_shouldThrow() {
        StockPostVm postVm = new StockPostVm(100L, 1L);
        when(stockRepository.existsByWarehouseIdAndProductId(1L, 100L)).thenReturn(false);
        when(productService.getProduct(100L)).thenReturn(null);

        assertThrows(NotFoundException.class,
            () -> stockService.addProductIntoWarehouse(List.of(postVm)));
    }

    @Test
    void addProductIntoWarehouse_whenWarehouseNotFound_shouldThrow() {
        StockPostVm postVm = new StockPostVm(100L, 1L);
        when(stockRepository.existsByWarehouseIdAndProductId(1L, 100L)).thenReturn(false);
        when(productService.getProduct(100L))
            .thenReturn(new ProductInfoVm(100L, "Product", "SKU", false));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
            () -> stockService.addProductIntoWarehouse(List.of(postVm)));
    }

    @Test
    void getStocksByWarehouseIdAndProductNameAndSku_shouldReturnStockVms() {
        ProductInfoVm productInfo = new ProductInfoVm(100L, "Product", "SKU", true);
        when(warehouseService.getProductWarehouse(1L, "name", "sku", FilterExistInWhSelection.YES))
            .thenReturn(List.of(productInfo));

        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .reservedQuantity(5L)
            .warehouse(warehouse)
            .build();
        when(stockRepository.findByWarehouseIdAndProductIdIn(any(), anyList()))
            .thenReturn(List.of(stock));

        List<StockVm> result = stockService.getStocksByWarehouseIdAndProductNameAndSku(1L, "name", "sku");

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).productId());
        assertEquals("Product", result.get(0).productName());
    }

    @Test
    void updateProductQuantityInStock_whenValid_shouldUpdateAndCreateHistory() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .reservedQuantity(5L)
            .warehouse(warehouse)
            .build();

        StockQuantityVm sqVm = new StockQuantityVm(1L, 10L, "Added 10");
        StockQuantityUpdateVm updateVm = new StockQuantityUpdateVm(List.of(sqVm));

        when(stockRepository.findAllById(anyList())).thenReturn(List.of(stock));

        stockService.updateProductQuantityInStock(updateVm);

        assertEquals(60L, stock.getQuantity());
        verify(stockRepository).saveAll(anyList());
        verify(stockHistoryService).createStockHistories(anyList(), anyList());
        verify(productService).updateProductQuantity(anyList());
    }

    @Test
    void updateProductQuantityInStock_whenStockQuantityVmNotFound_shouldContinue() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .reservedQuantity(5L)
            .warehouse(warehouse)
            .build();

        StockQuantityVm sqVm = new StockQuantityVm(999L, 10L, "No match");
        StockQuantityUpdateVm updateVm = new StockQuantityUpdateVm(List.of(sqVm));

        when(stockRepository.findAllById(anyList())).thenReturn(List.of(stock));

        stockService.updateProductQuantityInStock(updateVm);

        assertEquals(50L, stock.getQuantity());
    }

    @Test
    void updateProductQuantityInStock_whenQuantityIsNull_shouldDefaultToZero() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .reservedQuantity(5L)
            .warehouse(warehouse)
            .build();

        StockQuantityVm sqVm = new StockQuantityVm(1L, null, "Null qty");
        StockQuantityUpdateVm updateVm = new StockQuantityUpdateVm(List.of(sqVm));

        when(stockRepository.findAllById(anyList())).thenReturn(List.of(stock));

        stockService.updateProductQuantityInStock(updateVm);

        assertEquals(50L, stock.getQuantity());
    }

    @Test
    void updateProductQuantityInStock_whenEmptyStocks_shouldNotCallUpdateProduct() {
        StockQuantityVm sqVm = new StockQuantityVm(1L, 10L, "test");
        StockQuantityUpdateVm updateVm = new StockQuantityUpdateVm(List.of(sqVm));

        when(stockRepository.findAllById(anyList())).thenReturn(List.of());

        stockService.updateProductQuantityInStock(updateVm);

        verify(stockRepository).saveAll(anyList());
    }
}
