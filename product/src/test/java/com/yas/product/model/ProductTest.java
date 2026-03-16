package com.yas.product.model;

import static org.junit.jupiter.api.Assertions.*;

import com.yas.product.model.attribute.ProductAttributeValue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equals_sameReference_returnsTrue() {
        Product product = Product.builder().id(1L).build();
        assertEquals(product, product);
    }

    @Test
    void equals_sameId_returnsTrue() {
        Product product1 = Product.builder().id(1L).name("A").build();
        Product product2 = Product.builder().id(1L).name("B").build();
        assertEquals(product1, product2);
    }

    @Test
    void equals_differentId_returnsFalse() {
        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        assertNotEquals(product1, product2);
    }

    @Test
    void equals_nullId_returnsFalse() {
        Product product1 = Product.builder().id(null).build();
        Product product2 = Product.builder().id(1L).build();
        assertNotEquals(product1, product2);
    }

    @Test
    void equals_notProduct_returnsFalse() {
        Product product = Product.builder().id(1L).build();
        assertNotEquals(product, "not a product");
    }

    @Test
    void equals_null_returnsFalse() {
        Product product = Product.builder().id(1L).build();
        assertNotEquals(product, null);
    }

    @Test
    void hashCode_consistent() {
        Product product1 = Product.builder().id(1L).build();
        Product product2 = Product.builder().id(2L).build();
        assertEquals(product1.hashCode(), product2.hashCode());
    }

    // Test @Builder.Default fields - both with explicit values and defaults
    @Test
    void builder_defaultValues_createsEmptyLists() {
        Product product = Product.builder().id(1L).build();
        assertNotNull(product.getRelatedProducts());
        assertNotNull(product.getProductCategories());
        assertNotNull(product.getAttributeValues());
        assertNotNull(product.getProductImages());
        assertNotNull(product.getProducts());
        assertTrue(product.getRelatedProducts().isEmpty());
        assertTrue(product.getProductCategories().isEmpty());
    }

    @Test
    void builder_explicitDefaultValues_usesProvidedLists() {
        List<ProductRelated> related = new ArrayList<>();
        related.add(ProductRelated.builder().id(1L).build());
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(ProductCategory.builder().id(1L).build());
        List<ProductAttributeValue> attrValues = new ArrayList<>();
        List<ProductImage> images = new ArrayList<>();
        images.add(ProductImage.builder().id(1L).build());
        List<Product> children = new ArrayList<>();
        children.add(Product.builder().id(2L).build());

        Product product = Product.builder()
            .id(1L)
            .relatedProducts(related)
            .productCategories(categories)
            .attributeValues(attrValues)
            .productImages(images)
            .products(children)
            .build();

        assertEquals(1, product.getRelatedProducts().size());
        assertEquals(1, product.getProductCategories().size());
        assertEquals(0, product.getAttributeValues().size());
        assertEquals(1, product.getProductImages().size());
        assertEquals(1, product.getProducts().size());
    }

    @Test
    void builder_allFields() {
        Brand brand = new Brand();
        brand.setId(1L);
        Product parent = Product.builder().id(99L).build();

        Product product = Product.builder()
            .id(1L)
            .name("Test")
            .slug("test")
            .sku("SKU1")
            .gtin("GTIN1")
            .price(10.0)
            .hasOptions(true)
            .isAllowedToOrder(true)
            .isPublished(true)
            .isFeatured(true)
            .isVisibleIndividually(true)
            .stockTrackingEnabled(true)
            .stockQuantity(100L)
            .taxClassId(5L)
            .brand(brand)
            .parent(parent)
            .thumbnailMediaId(1L)
            .taxIncluded(true)
            .build();

        assertEquals(1L, product.getId());
        assertEquals("Test", product.getName());
        assertTrue(product.isHasOptions());
        assertTrue(product.isStockTrackingEnabled());
        assertEquals(100L, product.getStockQuantity());
        assertTrue(product.isTaxIncluded());
        assertEquals(parent, product.getParent());
    }
}

