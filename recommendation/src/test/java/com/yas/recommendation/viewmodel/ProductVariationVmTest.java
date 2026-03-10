package com.yas.recommendation.viewmodel;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductVariationVmTest {

    @Test
    void testRecordConstructor() {
        Map<Long, String> options = Map.of(1L, "Red", 2L, "Large");
        ProductVariationVm vm = new ProductVariationVm(
                1L,
                "Product Variant",
                "product-variant",
                "SKU123",
                "GTIN123",
                99.99,
                options
        );

        assertEquals(1L, vm.id());
        assertEquals("Product Variant", vm.name());
        assertEquals("product-variant", vm.slug());
        assertEquals("SKU123", vm.sku());
        assertEquals("GTIN123", vm.gtin());
        assertEquals(99.99, vm.price());
        assertEquals(options, vm.options());
    }

    @Test
    void testRecordEquality() {
        Map<Long, String> options = Map.of(1L, "Red");
        ProductVariationVm vm1 = new ProductVariationVm(1L, "Name", "slug", "sku", "gtin", 99.99, options);
        ProductVariationVm vm2 = new ProductVariationVm(1L, "Name", "slug", "sku", "gtin", 99.99, options);
        ProductVariationVm vm3 = new ProductVariationVm(2L, "Other", "other", "sku2", "gtin2", 49.99, Map.of());

        assertEquals(vm1, vm2);
        assertNotEquals(vm1, vm3);
    }

    @Test
    void testRecordHashCode() {
        Map<Long, String> options = Map.of(1L, "Red");
        ProductVariationVm vm1 = new ProductVariationVm(1L, "Name", "slug", "sku", "gtin", 99.99, options);
        ProductVariationVm vm2 = new ProductVariationVm(1L, "Name", "slug", "sku", "gtin", 99.99, options);

        assertEquals(vm1.hashCode(), vm2.hashCode());
    }

    @Test
    void testNullValues() {
        ProductVariationVm vm = new ProductVariationVm(null, null, null, null, null, null, null);

        assertNull(vm.id());
        assertNull(vm.name());
        assertNull(vm.slug());
        assertNull(vm.sku());
        assertNull(vm.gtin());
        assertNull(vm.price());
        assertNull(vm.options());
    }
}
