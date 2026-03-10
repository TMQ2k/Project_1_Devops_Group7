package com.yas.order.service;

import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.order.config.ServiceUrlConfig;
import com.yas.order.viewmodel.order.OrderItemVm;
import com.yas.order.viewmodel.order.OrderVm;
import com.yas.order.viewmodel.product.ProductCheckoutListVm;
import com.yas.order.viewmodel.product.ProductGetCheckoutListVm;
import com.yas.order.viewmodel.product.ProductQuantityItem;
import com.yas.order.viewmodel.product.ProductVariationVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ServiceUrlConfig serviceUrlConfig;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        when(serviceUrlConfig.product()).thenReturn("http://product-service");
        
        // Mock SecurityContext for JWT extraction
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        
        when(jwt.getTokenValue()).thenReturn("test-token");
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetProductVariations_whenValidProductId_thenReturnsVariations() {
        // Given
        Long productId = 1L;
        ProductVariationVm variation1 = new ProductVariationVm(1L, "Variation 1", "SKU1");
        ProductVariationVm variation2 = new ProductVariationVm(2L, "Variation 2", "SKU2");
        List<ProductVariationVm> expectedVariations = List.of(variation1, variation2);
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.ok(expectedVariations));
        
        // When
        List<ProductVariationVm> result = productService.getProductVariations(productId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Variation 1");
        assertThat(result.get(1).name()).isEqualTo("Variation 2");
        
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(any(URI.class));
    }

    @Test
    void testSubtractProductStockQuantity_whenValidOrder_thenCallsAPI() {
        // Given
        OrderItemVm item1 = new OrderItemVm(1L, 1L, "Product 1", 5, BigDecimal.valueOf(100), "Note1", 
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 100L);
        OrderItemVm item2 = new OrderItemVm(2L, 2L, "Product 2", 3, BigDecimal.valueOf(200), "Note2",
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 100L);
        OrderVm orderVm = OrderVm.builder()
            .orderItemVms(Set.of(item1, item2))
            .build();
        
        when(restClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(URI.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(anyList())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        
        // When
        productService.subtractProductStockQuantity(orderVm);
        
        // Then
        verify(restClient).put();
        verify(requestBodyUriSpec).uri(any(URI.class));
        verify(requestBodySpec).body(anyList());
    }

    @Test
    void testGetProductInfomation_whenValidIds_thenReturnsProductMap() {
        // Given
        Set<Long> ids = Set.of(1L, 2L);
        int pageNo = 0;
        int pageSize = 10;
        
        ProductCheckoutListVm product1 = ProductCheckoutListVm.builder()
            .id(1L)
            .name("Product 1")
            .price(100.0)
            .taxClassId(1L)
            .build();
        ProductCheckoutListVm product2 = ProductCheckoutListVm.builder()
            .id(2L)
            .name("Product 2")
            .price(200.0)
            .taxClassId(2L)
            .build();
        
        ProductGetCheckoutListVm response = new ProductGetCheckoutListVm(
            List.of(product1, product2), 0, 10, 2, 1, true
        );
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.ok(response));
        
        // When
        Map<Long, ProductCheckoutListVm> result = productService.getProductInfomation(ids, pageNo, pageSize);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(1L).getName()).isEqualTo("Product 1");
        assertThat(result.get(2L).getName()).isEqualTo("Product 2");
        
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(any(URI.class));
    }

    @Test
    void testGetProductInfomation_whenNullResponse_thenThrowsNotFoundException() {
        // Given
        Set<Long> ids = Set.of(1L);
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.ok(null));
        
        // When/Then
        assertThatThrownBy(() -> productService.getProductInfomation(ids, 0, 10))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("PRODUCT_NOT_FOUND");
    }

    @Test
    void testGetProductInfomation_whenNullProductList_thenThrowsNotFoundException() {
        // Given
        Set<Long> ids = Set.of(1L);
        ProductGetCheckoutListVm response = new ProductGetCheckoutListVm(null, 0, 10, 0, 0, false);
        
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(any(ParameterizedTypeReference.class)))
            .thenReturn(ResponseEntity.ok(response));
        
        // When/Then
        assertThatThrownBy(() -> productService.getProductInfomation(ids, 0, 10))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("PRODUCT_NOT_FOUND");
    }
}
