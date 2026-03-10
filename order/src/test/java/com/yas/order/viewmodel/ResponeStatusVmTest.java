package com.yas.order.viewmodel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponeStatusVmTest {

    @Test
    void testRecordConstructor() {
        ResponeStatusVm vm = new ResponeStatusVm("Success", "Operation completed", "200");

        assertNotNull(vm);
        assertEquals("Success", vm.title());
        assertEquals("Operation completed", vm.message());
        assertEquals("200", vm.statusCode());
    }

    @Test
    void testRecordWithNullValues() {
        ResponeStatusVm vm = new ResponeStatusVm(null, null, null);

        assertNotNull(vm);
        assertEquals(null, vm.title());
        assertEquals(null, vm.message());
        assertEquals(null, vm.statusCode());
    }

    @Test
    void testRecordEquality() {
        ResponeStatusVm vm1 = new ResponeStatusVm("Error", "Failed", "500");
        ResponeStatusVm vm2 = new ResponeStatusVm("Error", "Failed", "500");

        assertEquals(vm1, vm2);
    }
}
