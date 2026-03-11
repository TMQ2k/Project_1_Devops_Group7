package com.yas.inventory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.inventory.model.Stock;
import com.yas.inventory.model.StockHistory;
import com.yas.inventory.model.Warehouse;
import com.yas.inventory.repository.StockHistoryRepository;
import com.yas.inventory.viewmodel.product.ProductInfoVm;
import com.yas.inventory.viewmodel.stock.StockQuantityVm;
import com.yas.inventory.viewmodel.stockhistory.StockHistoryListVm;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockHistoryServiceTest {

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @Mock
    private ProductService productService;

    private StockHistoryService stockHistoryService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        stockHistoryService = new StockHistoryService(stockHistoryRepository, productService);
        warehouse = Warehouse.builder().id(1L).name("WH1").addressId(10L).build();
    }

    @Test
    void createStockHistories_whenMatching_shouldSaveHistories() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .warehouse(warehouse)
            .build();

        StockQuantityVm sqVm = new StockQuantityVm(1L, 10L, "Added stock");

        stockHistoryService.createStockHistories(List.of(stock), List.of(sqVm));

        verify(stockHistoryRepository).saveAll(anyList());
    }

    @Test
    void createStockHistories_whenNoMatch_shouldSaveEmptyList() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(50L)
            .warehouse(warehouse)
            .build();

        StockQuantityVm sqVm = new StockQuantityVm(999L, 10L, "No match");

        stockHistoryService.createStockHistories(List.of(stock), List.of(sqVm));

        verify(stockHistoryRepository).saveAll(anyList());
    }

    @Test
    void getStockHistories_shouldReturnStockHistoryListVm() {
        StockHistory history = StockHistory.builder()
            .id(1L)
            .productId(100L)
            .adjustedQuantity(10L)
            .note("test note")
            .warehouse(warehouse)
            .build();

        when(stockHistoryRepository.findByProductIdAndWarehouseIdOrderByCreatedOnDesc(100L, 1L))
            .thenReturn(List.of(history));
        when(productService.getProduct(100L))
            .thenReturn(new ProductInfoVm(100L, "Product", "SKU", true));

        StockHistoryListVm result = stockHistoryService.getStockHistories(100L, 1L);

        assertNotNull(result);
        assertEquals(1, result.data().size());
        assertEquals("Product", result.data().get(0).productName());
    }
}
