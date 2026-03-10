package com.yas.recommendation.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductAttributeValueVmTest {

    @Test
    void testRecordConstructor() {
        ProductAttributeValueVm vm = new ProductAttributeValueVm(1L, "Color", "Red");

        assertEquals(1L, vm.id());
        assertEquals("Color", vm.nameProductAttribute());
        assertEquals("Red", vm.value());
    }

    @Test
    void testRecordEquality() {
        ProductAttributeValueVm vm1 = new ProductAttributeValueVm(1L, "Color", "Red");
        ProductAttributeValueVm vm2 = new ProductAttributeValueVm(1L, "Color", "Red");
        ProductAttributeValueVm vm3 = new ProductAttributeValueVm(2L, "Size", "Large");

        assertEquals(vm1, vm2);
        assertNotEquals(vm1, vm3);
    }

    @Test
    void testRecordHashCode() {
        ProductAttributeValueVm vm1 = new ProductAttributeValueVm(1L, "Color", "Red");
        ProductAttributeValueVm vm2 = new ProductAttributeValueVm(1L, "Color", "Red");

        assertEquals(vm1.hashCode(), vm2.hashCode());
    }

    @Test
    void testNullValues() {
        ProductAttributeValueVm vm = new ProductAttributeValueVm(0L, null, null);

        assertEquals(0L, vm.id());
        assertNull(vm.nameProductAttribute());
        assertNull(vm.value());
    }
}
