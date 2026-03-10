package com.yas.rating.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class RatingTest {

    @Test
    void testEquals_whenSameObject_thenReturnTrue() {
        Rating rating = Rating.builder().id(1L).build();
        assertTrue(rating.equals(rating));
    }

    @Test
    void testEquals_whenDifferentObjectSameId_thenReturnTrue() {
        Rating rating1 = Rating.builder().id(1L).build();
        Rating rating2 = Rating.builder().id(1L).build();
        assertTrue(rating1.equals(rating2));
    }

    @Test
    void testEquals_whenDifferentObjectDifferentId_thenReturnFalse() {
        Rating rating1 = Rating.builder().id(1L).build();
        Rating rating2 = Rating.builder().id(2L).build();
        assertFalse(rating1.equals(rating2));
    }

    @Test
    void testEquals_whenNullId_thenReturnFalse() {
        Rating rating1 = Rating.builder().id(null).build();
        Rating rating2 = Rating.builder().id(1L).build();
        assertFalse(rating1.equals(rating2));
    }

    @Test
    void testEquals_whenNotRatingObject_thenReturnFalse() {
        Rating rating = Rating.builder().id(1L).build();
        Object other = new Object();
        assertFalse(rating.equals(other));
    }

    @Test
    void testHashCode_thenReturnClassHashCode() {
        Rating rating = Rating.builder().id(1L).build();
        assertEquals(Rating.class.hashCode(), rating.hashCode());
    }
}
