package com.yas.product.viewmodel.product;

import static org.junit.jupiter.api.Assertions.*;

import com.yas.product.model.Brand;
import com.yas.product.model.Product;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductViewModelTest {

    private Product product;
    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(10L);
        brand.setName("TestBrand");
        brand.setSlug("test-brand");

        product = Product.builder()
            .id(1L)
            .name("Test Product")
            .slug("test-product")
            .sku("SKU-001")
            .gtin("GTIN-001")
            .price(99.99)
            .description("Full description")
            .shortDescription("Short desc")
            .isAllowedToOrder(true)
            .isPublished(true)
            .isFeatured(true)
            .isVisibleIndividually(true)
            .taxClassId(5L)
            .brand(brand)
            .build();
        product.setCreatedOn(ZonedDateTime.now());
        product.setCreatedBy("admin");
        product.setLastModifiedOn(ZonedDateTime.now());
        product.setLastModifiedBy("admin");
    }

    // ProductListVm tests
    @Test
    void productListVm_fromModel_withParent() {
        Product parent = Product.builder().id(100L).build();
        product.setParent(parent);
        ProductListVm vm = ProductListVm.fromModel(product);
        assertEquals(1L, vm.id());
        assertEquals("Test Product", vm.name());
        assertEquals("test-product", vm.slug());
        assertTrue(vm.isAllowedToOrder());
        assertTrue(vm.isPublished());
        assertTrue(vm.isFeatured());
        assertTrue(vm.isVisibleIndividually());
        assertEquals(99.99, vm.price());
        assertEquals(5L, vm.taxClassId());
        assertEquals(100L, vm.parentId());
    }

    @Test
    void productListVm_fromModel_withoutParent() {
        product.setParent(null);
        ProductListVm vm = ProductListVm.fromModel(product);
        assertNull(vm.parentId());
    }

    // ProductGetDetailVm tests
    @Test
    void productGetDetailVm_fromModel() {
        ProductGetDetailVm vm = ProductGetDetailVm.fromModel(product);
        assertEquals(1L, vm.id());
        assertEquals("Test Product", vm.name());
        assertEquals("test-product", vm.slug());
    }

    // ProductInfoVm tests
    @Test
    void productInfoVm_fromProduct() {
        ProductInfoVm vm = ProductInfoVm.fromProduct(product);
        assertEquals(1L, vm.id());
        assertEquals("Test Product", vm.name());
        assertEquals("SKU-001", vm.sku());
    }

    // ProductCheckoutListVm tests
    @Test
    void productCheckoutListVm_fromModel_withParent() {
        Product parent = Product.builder().id(200L).build();
        product.setParent(parent);
        ProductCheckoutListVm vm = ProductCheckoutListVm.fromModel(product);
        assertEquals(1L, vm.id());
        assertEquals("Test Product", vm.name());
        assertEquals("Full description", vm.description());
        assertEquals("Short desc", vm.shortDescription());
        assertEquals("SKU-001", vm.sku());
        assertEquals(200L, vm.parentId());
        assertEquals(10L, vm.brandId());
        assertEquals(99.99, vm.price());
        assertEquals(5L, vm.taxClassId());
        assertEquals("", vm.thumbnailUrl());
    }

    @Test
    void productCheckoutListVm_fromModel_withoutParent() {
        product.setParent(null);
        ProductCheckoutListVm vm = ProductCheckoutListVm.fromModel(product);
        assertNull(vm.parentId());
    }
}
