package com.yas.recommendation.vector.product.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.kafka.cdc.message.Product;
import com.yas.recommendation.vector.product.store.ProductVectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductVectorSyncServiceTest {

  private ProductVectorSyncService service;
  private ProductVectorRepository repository;

  @BeforeEach
  void setUp() {
    repository = mock(ProductVectorRepository.class);
    service = new ProductVectorSyncService(repository);
  }

  @Test
  void createProductVector_whenProductIsPublished_thenAddToRepository() {
    // Given
    Product product = mock(Product.class);
    when(product.isPublished()).thenReturn(true);
    when(product.getId()).thenReturn(1L);

    // When
    service.createProductVector(product);

    // Then
    verify(repository).add(1L);
  }

  @Test
  void createProductVector_whenProductIsNotPublished_thenDoNotAddToRepository() {
    // Given
    Product product = mock(Product.class);
    when(product.isPublished()).thenReturn(false);
    when(product.getId()).thenReturn(1L);

    // When
    service.createProductVector(product);

    // Then
    verify(repository, never()).add(1L);
  }

  @Test
  void updateProductVector_whenProductIsPublished_thenUpdateInRepository() {
    // Given
    Product product = mock(Product.class);
    when(product.isPublished()).thenReturn(true);
    when(product.getId()).thenReturn(1L);

    // When
    service.updateProductVector(product);

    // Then
    verify(repository).update(1L);
    verify(repository, never()).delete(1L);
  }

  @Test
  void updateProductVector_whenProductIsNotPublished_thenDeleteFromRepository() {
    // Given
    Product product = mock(Product.class);
    when(product.isPublished()).thenReturn(false);
    when(product.getId()).thenReturn(1L);

    // When
    service.updateProductVector(product);

    // Then
    verify(repository).delete(1L);
    verify(repository, never()).update(1L);
  }

  @Test
  void deleteProductVector_thenDeleteFromRepository() {
    // Given
    Long productId = 1L;

    // When
    service.deleteProductVector(productId);

    // Then
    verify(repository).delete(productId);
  }
}
