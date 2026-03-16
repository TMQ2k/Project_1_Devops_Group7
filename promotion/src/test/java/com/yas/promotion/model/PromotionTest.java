package com.yas.promotion.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    void testEquals_whenSameObject_thenReturnTrue() {
        Promotion promotion = Promotion.builder().id(1L).build();
        assertTrue(promotion.equals(promotion));
    }

    @Test
    void testEquals_whenDifferentObjectSameId_thenReturnTrue() {
        Promotion promotion1 = Promotion.builder().id(1L).build();
        Promotion promotion2 = Promotion.builder().id(1L).build();
        assertTrue(promotion1.equals(promotion2));
    }

    @Test
    void testEquals_whenDifferentObjectDifferentId_thenReturnFalse() {
        Promotion promotion1 = Promotion.builder().id(1L).build();
        Promotion promotion2 = Promotion.builder().id(2L).build();
        assertFalse(promotion1.equals(promotion2));
    }

    @Test
    void testEquals_whenNullId_thenReturnFalse() {
        Promotion promotion1 = Promotion.builder().id(null).build();
        Promotion promotion2 = Promotion.builder().id(1L).build();
        assertFalse(promotion1.equals(promotion2));
    }

    @Test
    void testEquals_whenNotPromotionObject_thenReturnFalse() {
        Promotion promotion = Promotion.builder().id(1L).build();
        Object other = new Object();
        assertFalse(promotion.equals(other));
    }

    @Test
    void testHashCode_thenReturnClassHashCode() {
        Promotion promotion = Promotion.builder().id(1L).build();
        assertEquals(Promotion.class.hashCode(), promotion.hashCode());
    }

    @Test
    void testSetPromotionApplies_whenPromotionAppliesIsNull_thenCreateList() {
        Promotion promotion = new Promotion();
        List<PromotionApply> applies = new ArrayList<>();
        applies.add(new PromotionApply());

        promotion.setPromotionApplies(applies);

        assertNotNull(promotion.getPromotionApplies());
        assertEquals(1, promotion.getPromotionApplies().size());
    }

    @Test
    void testSetPromotionApplies_whenPromotionAppliesExists_thenClearAndAddNew() {
        Promotion promotion = Promotion.builder()
            .promotionApplies(new ArrayList<>())
            .build();
        promotion.getPromotionApplies().add(new PromotionApply());

        List<PromotionApply> newApplies = new ArrayList<>();
        newApplies.add(new PromotionApply());
        newApplies.add(new PromotionApply());

        promotion.setPromotionApplies(newApplies);

        assertEquals(2, promotion.getPromotionApplies().size());
    }
}
