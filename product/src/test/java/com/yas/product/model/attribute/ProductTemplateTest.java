package com.yas.product.model.attribute;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductTemplateTest {

    @Test
    void equals_sameReference() {
        ProductTemplate pt = ProductTemplate.builder().id(1L).build();
        assertEquals(pt, pt);
    }

    @Test
    void equals_sameId() {
        ProductTemplate pt1 = ProductTemplate.builder().id(1L).build();
        ProductTemplate pt2 = ProductTemplate.builder().id(1L).build();
        assertEquals(pt1, pt2);
    }

    @Test
    void equals_differentId() {
        ProductTemplate pt1 = ProductTemplate.builder().id(1L).build();
        ProductTemplate pt2 = ProductTemplate.builder().id(2L).build();
        assertNotEquals(pt1, pt2);
    }

    @Test
    void equals_nullId() {
        ProductTemplate pt1 = ProductTemplate.builder().id(null).build();
        ProductTemplate pt2 = ProductTemplate.builder().id(1L).build();
        assertNotEquals(pt1, pt2);
    }

    @Test
    void equals_wrongType() {
        ProductTemplate pt = ProductTemplate.builder().id(1L).build();
        assertNotEquals(pt, "string");
    }

    @Test
    void hashCode_consistent() {
        ProductTemplate pt1 = ProductTemplate.builder().id(1L).build();
        ProductTemplate pt2 = ProductTemplate.builder().id(2L).build();
        assertEquals(pt1.hashCode(), pt2.hashCode());
    }

    @Test
    void builder_defaultProductAttributeTemplates() {
        ProductTemplate pt = ProductTemplate.builder().id(1L).build();
        assertNotNull(pt.getProductAttributeTemplates());
        assertTrue(pt.getProductAttributeTemplates().isEmpty());
    }

    @Test
    void builder_explicitProductAttributeTemplates() {
        List<ProductAttributeTemplate> templates = new ArrayList<>();
        templates.add(new ProductAttributeTemplate());
        ProductTemplate pt = ProductTemplate.builder()
                .id(1L)
                .productAttributeTemplates(templates)
                .build();
        assertEquals(1, pt.getProductAttributeTemplates().size());
    }
}
