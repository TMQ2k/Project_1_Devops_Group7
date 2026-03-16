package com.yas.inventory.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.yas.inventory.model.Stock;
import com.yas.inventory.model.Warehouse;
import com.yas.inventory.viewmodel.error.ErrorVm;
import com.yas.inventory.viewmodel.product.ProductInfoVm;
import com.yas.inventory.viewmodel.product.ProductQuantityPostVm;
import com.yas.inventory.viewmodel.stock.StockPostVm;
import com.yas.inventory.viewmodel.stock.StockQuantityUpdateVm;
import com.yas.inventory.viewmodel.stock.StockQuantityVm;
import com.yas.inventory.viewmodel.stock.StockVm;
import java.util.List;
import org.junit.jupiter.api.Test;

class ViewModelTest {

    @Test
    void errorVm_withFieldErrors_shouldCreateCorrectly() {
        List<String> errors = List.of("field1 invalid", "field2 required");
        ErrorVm vm = new ErrorVm("400", "Bad Request", "Validation error", errors);

        assertEquals("400", vm.statusCode());
        assertEquals("Bad Request", vm.title());
        assertEquals("Validation error", vm.detail());
        assertEquals(2, vm.fieldErrors().size());
    }

    @Test
    void errorVm_withoutFieldErrors_shouldCreateEmptyList() {
        ErrorVm vm = new ErrorVm("404", "Not Found", "Resource not found");

        assertEquals("404", vm.statusCode());
        assertEquals("Not Found", vm.title());
        assertEquals("Resource not found", vm.detail());
        assertNotNull(vm.fieldErrors());
        assertTrue(vm.fieldErrors().isEmpty());
    }

    @Test
    void stockVm_fromModel_shouldMapCorrectly() {
        Warehouse wh = Warehouse.builder().id(1L).name("WH").build();
        Stock stock = Stock.builder()
            .id(10L)
            .productId(100L)
            .quantity(50L)
            .reservedQuantity(5L)
            .warehouse(wh)
            .build();
        ProductInfoVm productInfo = new ProductInfoVm(100L, "Product", "SKU-1", true);

        StockVm result = StockVm.fromModel(stock, productInfo);

        assertEquals(10L, result.id());
        assertEquals(100L, result.productId());
        assertEquals("Product", result.productName());
        assertEquals("SKU-1", result.productSku());
        assertEquals(50L, result.quantity());
        assertEquals(5L, result.reservedQuantity());
        assertEquals(1L, result.warehouseId());
    }

    @Test
    void stockPostVm_shouldHoldValues() {
        StockPostVm vm = new StockPostVm(1L, 2L);
        assertEquals(1L, vm.productId());
        assertEquals(2L, vm.warehouseId());
    }

    @Test
    void stockQuantityVm_shouldHoldValues() {
        StockQuantityVm vm = new StockQuantityVm(1L, 100L, "note");
        assertEquals(1L, vm.stockId());
        assertEquals(100L, vm.quantity());
        assertEquals("note", vm.note());
    }

    @Test
    void stockQuantityUpdateVm_shouldHoldValues() {
        StockQuantityVm sqVm = new StockQuantityVm(1L, 10L, "n");
        StockQuantityUpdateVm vm = new StockQuantityUpdateVm(List.of(sqVm));
        assertEquals(1, vm.stockQuantityList().size());
    }

    @Test
    void productQuantityPostVm_fromModel_shouldMapCorrectly() {
        Stock stock = Stock.builder()
            .id(1L)
            .productId(100L)
            .quantity(75L)
            .warehouse(Warehouse.builder().id(1L).build())
            .build();

        ProductQuantityPostVm result = ProductQuantityPostVm.fromModel(stock);

        assertEquals(100L, result.productId());
        assertEquals(75L, result.stockQuantity());
    }

    @Test
    void productInfoVm_shouldHoldValues() {
        ProductInfoVm vm = new ProductInfoVm(1L, "Name", "SKU", true);
        assertEquals(1L, vm.id());
        assertEquals("Name", vm.name());
        assertEquals("SKU", vm.sku());
        assertTrue(vm.existInWh());
    }
}
