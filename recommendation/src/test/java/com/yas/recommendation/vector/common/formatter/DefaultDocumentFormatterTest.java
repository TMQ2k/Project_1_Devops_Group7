package com.yas.recommendation.vector.common.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import tools.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultDocumentFormatterTest {

  private DefaultDocumentFormatter formatter;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    formatter = new DefaultDocumentFormatter();
    objectMapper = new ObjectMapper();
  }

  @Test
  void format_whenTemplateHasPlaceholders_thenReplaceWithValues() {
    // Given
    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("name", "Test Document");
    entityMap.put("description", "This is a test");

    String template = "Document: {name}, Description: {description}";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).isEqualTo("Document: Test Document, Description: This is a test");
  }

  @Test
  void format_whenTemplateHasHtmlTags_thenRemoveThem() {
    // Given
    Map<String, Object> entityMap = new HashMap<>();
    entityMap.put("content", "Test");

    String template = "<p>{content}</p>";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).doesNotContain("<p>");
    assertThat(result).doesNotContain("</p>");
    assertThat(result).contains("Test");
  }

  @Test
  void format_whenNoPlaceholders_thenReturnTemplate() {
    // Given
    Map<String, Object> entityMap = new HashMap<>();
    String template = "Simple text without placeholders";

    // When
    String result = formatter.format(entityMap, template, objectMapper);

    // Then
    assertThat(result).isEqualTo(template);
  }
}
