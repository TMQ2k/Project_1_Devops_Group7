package com.yas.commonlibrary.kafka.cdc.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void testEnumValues() {
        assertEquals(4, Operation.values().length);
        assertEquals(Operation.READ, Operation.valueOf("READ"));
        assertEquals(Operation.CREATE, Operation.valueOf("CREATE"));
        assertEquals(Operation.UPDATE, Operation.valueOf("UPDATE"));
        assertEquals(Operation.DELETE, Operation.valueOf("DELETE"));
    }

    @Test
    void testGetName() {
        assertEquals("r", Operation.READ.getName());
        assertEquals("c", Operation.CREATE.getName());
        assertEquals("u", Operation.UPDATE.getName());
        assertEquals("d", Operation.DELETE.getName());
    }

    @Test
    void testEnumEquality() {
        Operation op1 = Operation.CREATE;
        Operation op2 = Operation.CREATE;
        Operation op3 = Operation.UPDATE;

        assertEquals(op1, op2);
        assertNotEquals(op1, op3);
    }
}
