package com.yas.recommendation.vector.product.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import tools.jackson.databind.ObjectMapper;
import com.yas.recommendation.viewmodel.CategoryVm;
import com.yas.recommendation.viewmodel.ProductAttributeValueVm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductDocumentFormatterTest {

  private ProductDocumentFormatter formatter;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    formatter = new ProductDocumentFormatter();
    objectMapper = new ObjectMapper();
  }

  @Test
  void format_whenAttributeValuesAndCategoriesArePresent_thenFormatCorrectly() {
    // Given
    Map<String, Object> attribute1 = new HashMap<>();
    attribute1.put("id", 1L);
    attribute1.put("nameProductAttribute", "Color");
    attribute1.put("value", "Red");

    Map<String, Object> category1 = new HashMap<>();
    category1.put("id", 1L);
    category1.put("name", "Electronics");

    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("name", "Test Product");
    entityMap.put("attributeValues", List.of(attribute1));
    entityMap.put("categories", List.of(category1));

    String template = "Product: {name}, Attributes: {attributeValues}, Categories: {categories}";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).contains("Test Product");
    assertThat(result).contains("[Color: Red]");
    assertThat(result).contains("[Electronics]");
  }

  @Test
  void format_whenAttributeValuesIsNull_thenReturnEmptyBrackets() {
    // Given
    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("name", "Test Product");
    entityMap.put("attributeValues", null);
    entityMap.put("categories", List.of());

    String template = "Attributes: {attributeValues}";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).contains("[]");
  }

  @Test
  void format_whenCategoriesIsNull_thenReturnEmptyBrackets() {
    // Given
    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("name", "Test Product");
    entityMap.put("attributeValues", List.of());
    entityMap.put("categories", null);

    String template = "Categories: {categories}";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).contains("[]");
  }

  @Test
  void format_whenMultipleAttributesAndCategories_thenFormatWithCommas() {
    // Given
    Map<String, Object> attribute1 = new HashMap<>();
    attribute1.put("id", 1L);
    attribute1.put("nameProductAttribute", "Color");
    attribute1.put("value", "Red");

    Map<String, Object> attribute2 = new HashMap<>();
    attribute2.put("id", 2L);
    attribute2.put("nameProductAttribute", "Size");
    attribute2.put("value", "Large");

    Map<String, Object> category1 = new HashMap<>();
    category1.put("id", 1L);
    category1.put("name", "Electronics");

    Map<String, Object> category2 = new HashMap<>();
    category2.put("id", 2L);
    category2.put("name", "Gadgets");

    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("attributeValues", List.of(attribute1, attribute2));
    entityMap.put("categories", List.of(category1, category2));

    String template = "Attributes: {attributeValues}, Categories: {categories}";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).contains("Color: Red, Size: Large");
    assertThat(result).contains("Electronics, Gadgets");
  }
}
