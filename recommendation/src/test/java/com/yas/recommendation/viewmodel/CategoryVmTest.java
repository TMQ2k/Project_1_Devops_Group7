package com.yas.recommendation.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryVmTest {

    @Test
    void testRecordConstructor() {
        CategoryVm vm = new CategoryVm(
                1L,
                "Electronics",
                "Electronic items",
                "electronics",
                "electronics, tech",
                "Buy electronics",
                (short) 1,
                true
        );

        assertEquals(1L, vm.id());
        assertEquals("Electronics", vm.name());
        assertEquals("Electronic items", vm.description());
        assertEquals("electronics", vm.slug());
        assertEquals("electronics, tech", vm.metaKeyword());
        assertEquals("Buy electronics", vm.metaDescription());
        assertEquals((short) 1, vm.displayOrder());
        assertTrue(vm.isPublished());
    }

    @Test
    void testRecordEquality() {
        CategoryVm vm1 = new CategoryVm(1L, "Electronics", "desc", "slug", "key", "meta", (short) 1, true);
        CategoryVm vm2 = new CategoryVm(1L, "Electronics", "desc", "slug", "key", "meta", (short) 1, true);
        CategoryVm vm3 = new CategoryVm(2L, "Books", "desc", "slug", "key", "meta", (short) 2, false);

        assertEquals(vm1, vm2);
        assertNotEquals(vm1, vm3);
    }

    @Test
    void testRecordHashCode() {
        CategoryVm vm1 = new CategoryVm(1L, "Electronics", "desc", "slug", "key", "meta", (short) 1, true);
        CategoryVm vm2 = new CategoryVm(1L, "Electronics", "desc", "slug", "key", "meta", (short) 1, true);

        assertEquals(vm1.hashCode(), vm2.hashCode());
    }

    @Test
    void testNullValues() {
        CategoryVm vm = new CategoryVm(null, null, null, null, null, null, null, null);

        assertNull(vm.id());
        assertNull(vm.name());
        assertNull(vm.description());
        assertNull(vm.slug());
        assertNull(vm.metaKeyword());
        assertNull(vm.metaDescription());
        assertNull(vm.displayOrder());
        assertNull(vm.isPublished());
    }
}
