package com.yas.commonlibrary.model;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AbstractAuditEntityTest {

    // Concrete implementation for testing
    private static class TestAuditEntity extends AbstractAuditEntity {
    }

    @Test
    void testSettersAndGetters() {
        TestAuditEntity entity = new TestAuditEntity();
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime later = now.plusHours(1);

        entity.setCreatedOn(now);
        entity.setCreatedBy("user1");
        entity.setLastModifiedOn(later);
        entity.setLastModifiedBy("user2");

        assertEquals(now, entity.getCreatedOn());
        assertEquals("user1", entity.getCreatedBy());
        assertEquals(later, entity.getLastModifiedOn());
        assertEquals("user2", entity.getLastModifiedBy());
    }

    @Test
    void testDefaultValues() {
        TestAuditEntity entity = new TestAuditEntity();

        assertNull(entity.getCreatedOn());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getLastModifiedOn());
        assertNull(entity.getLastModifiedBy());
    }

    @Test
    void testTimestampModification() {
        TestAuditEntity entity = new TestAuditEntity();
        ZonedDateTime createdTime = ZonedDateTime.now();
        
        entity.setCreatedOn(createdTime);
        entity.setCreatedBy("admin");

        // Simulate modification
        ZonedDateTime modifiedTime = createdTime.plusMinutes(30);
        entity.setLastModifiedOn(modifiedTime);
        entity.setLastModifiedBy("moderator");

        assertEquals(createdTime, entity.getCreatedOn());
        assertEquals("admin", entity.getCreatedBy());
        assertTrue(entity.getLastModifiedOn().isAfter(entity.getCreatedOn()));
        assertEquals("moderator", entity.getLastModifiedBy());
    }
}
