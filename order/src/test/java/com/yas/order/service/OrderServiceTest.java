package com.yas.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.order.mapper.OrderMapper;
import com.yas.order.model.Order;
import com.yas.order.model.OrderAddress;
import com.yas.order.model.OrderItem;
import com.yas.order.model.enumeration.OrderStatus;
import com.yas.order.model.enumeration.PaymentStatus;
import com.yas.order.repository.OrderItemRepository;
import com.yas.order.repository.OrderRepository;
import com.yas.order.viewmodel.order.OrderGetVm;
import com.yas.order.viewmodel.order.PaymentOrderStatusVm;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCheckoutId("checkout123");
        testOrder.setOrderStatus(OrderStatus.PENDING);
        testOrder.setPaymentStatus(PaymentStatus.PENDING);
        testOrder.setCreatedBy("user123");
        testOrder.setCreatedOn(ZonedDateTime.now());
        
        // Set up OrderAddress objects to avoid NullPointerException
        OrderAddress shippingAddress = new OrderAddress();
        shippingAddress.setId(1L);
        shippingAddress.setContactName("Test User");
        
        OrderAddress billingAddress = new OrderAddress();
        billingAddress.setId(2L);
        billingAddress.setContactName("Test User");
        
        testOrder.setShippingAddressId(shippingAddress);
        testOrder.setBillingAddressId(billingAddress);
    }

    @Test
    void testFindOrderByCheckoutId_whenOrderExists_returnsOrder() {
        when(orderRepository.findByCheckoutId("checkout123")).thenReturn(Optional.of(testOrder));

        Order result = orderService.findOrderByCheckoutId("checkout123");

        assertThat(result).isNotNull();
        assertThat(result.getCheckoutId()).isEqualTo("checkout123");
        verify(orderRepository).findByCheckoutId("checkout123");
    }

    @Test
    void testFindOrderByCheckoutId_whenOrderNotExists_throwsNotFoundException() {
        when(orderRepository.findByCheckoutId("invalid")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findOrderByCheckoutId("invalid"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("invalid");
    }

    @Test
    void testUpdateOrderPaymentStatus_whenValidInput_updatesAndReturnsStatus() {
        PaymentOrderStatusVm statusVm = PaymentOrderStatusVm.builder()
            .orderId(1L)
            .orderStatus(OrderStatus.PAID.name())
            .paymentId(100L)
            .paymentStatus(PaymentStatus.COMPLETED.name())
            .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        PaymentOrderStatusVm result = orderService.updateOrderPaymentStatus(statusVm);

        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(1L);
        assertThat(result.paymentId()).isEqualTo(100L);
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getPaymentId()).isEqualTo(100L);
        assertThat(savedOrder.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void testUpdateOrderPaymentStatus_whenOrderNotFound_throwsNotFoundException() {
        PaymentOrderStatusVm statusVm = PaymentOrderStatusVm.builder()
            .orderId(999L)
            .orderStatus(OrderStatus.PAID.name())
            .paymentId(100L)
            .paymentStatus(PaymentStatus.COMPLETED.name())
            .build();

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderPaymentStatus(statusVm))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void testRejectOrder_whenOrderExists_updatesStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        orderService.rejectOrder(1L, "Out of stock");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getRejectReason()).isEqualTo("Out of stock");
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.REJECT);
    }

    @Test
    void testRejectOrder_whenOrderNotExists_throwsNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.rejectOrder(999L, "Test reason"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void testAcceptOrder_whenOrderExists_updatesStatus() {
        testOrder.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        orderService.acceptOrder(1L);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @Test
    void testAcceptOrder_whenOrderNotExists_throwsNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.acceptOrder(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void testGetOrderWithItemsById_whenOrderExists_returnsOrderVm() {
        List<OrderItem> orderItems = new ArrayList<>();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderItemRepository.findAllByOrderId(1L)).thenReturn(orderItems);

        // The actual method returns OrderVm, but we're testing the flow
        // This test verifies that the method calls the right repository methods
        orderService.getOrderWithItemsById(1L);

        verify(orderRepository).findById(1L);
        verify(orderItemRepository).findAllByOrderId(1L);
    }

    @Test
    void testGetOrderWithItemsById_whenOrderNotExists_throwsNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderWithItemsById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("999");
    }

    @Test
    void testGetLatestOrders_whenOrdersExist_returnsOrderList() {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.getLatestOrders(any())).thenReturn(orders);

        // When
        var result = orderService.getLatestOrders(5);

        // Then
        assertThat(result).isNotNull();
        verify(orderRepository).getLatestOrders(any());
    }

    @Test
    void testFindOrderVmByCheckoutId_whenOrderExists_returnsOrderGetVm() {
        // Given
        List<OrderItem> orderItems = new ArrayList<>();
        when(orderRepository.findByCheckoutId("checkout123")).thenReturn(Optional.of(testOrder));
        when(orderItemRepository.findAllByOrderId(1L)).thenReturn(orderItems);

        // When
        OrderGetVm result = orderService.findOrderVmByCheckoutId("checkout123");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        verify(orderRepository).findByCheckoutId("checkout123");
        verify(orderItemRepository).findAllByOrderId(1L);
    }

    @Test
    void testGetMyOrders_whenCalled_returnsUserOrders() {
        // Mock SecurityContext for authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        
        when(jwt.getSubject()).thenReturn("user123");
        when(authentication.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(orders);

        // When
        List<OrderGetVm> result = orderService.getMyOrders(null, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(orderRepository).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    void testGetLatestOrders_whenCountIsZero_returnsEmptyList() {
        // When
        var result = orderService.getLatestOrders(0);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetLatestOrders_whenCountIsNegative_returnsEmptyList() {
        // When
        var result = orderService.getLatestOrders(-1);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetLatestOrders_whenNoOrders_returnsEmptyList() {
        // Given
        when(orderRepository.getLatestOrders(any())).thenReturn(List.of());

        // When
        var result = orderService.getLatestOrders(5);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testIsOrderCompletedWithUserIdAndProductId_whenOrderExists_returnsTrue() {
        // Mock SecurityContext for authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        
        when(jwt.getSubject()).thenReturn("user123");
        when(authentication.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Given
        Long productId = 1L;
        when(productService.getProductVariations(productId)).thenReturn(List.of());
        when(orderRepository.findOne(any(Specification.class))).thenReturn(Optional.of(testOrder));

        // When
        var result = orderService.isOrderCompletedWithUserIdAndProductId(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        verify(productService).getProductVariations(productId);
    }

    @Test
    void testIsOrderCompletedWithUserIdAndProductId_whenOrderNotExists_returnsFalse() {
        // Mock SecurityContext for authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        
        when(jwt.getSubject()).thenReturn("user123");
        when(authentication.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Given
        Long productId = 1L;
        when(productService.getProductVariations(productId)).thenReturn(List.of());
        when(orderRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        // When
        var result = orderService.isOrderCompletedWithUserIdAndProductId(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void testIsOrderCompletedWithUserIdAndProductId_whenHasProductVariations_checksAllVariations() {
        // Mock SecurityContext for authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        
        when(jwt.getSubject()).thenReturn("user123");
        when(authentication.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Given
        Long productId = 1L;
        var variation1 = new com.yas.order.viewmodel.product.ProductVariationVm(2L, "Variation 1", "SKU1");
        var variation2 = new com.yas.order.viewmodel.product.ProductVariationVm(3L, "Variation 2", "SKU2");
        when(productService.getProductVariations(productId)).thenReturn(List.of(variation1, variation2));
        when(orderRepository.findOne(any(Specification.class))).thenReturn(Optional.of(testOrder));

        // When
        var result = orderService.isOrderCompletedWithUserIdAndProductId(productId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isPresent()).isTrue();
        verify(productService).getProductVariations(productId);
        verify(orderRepository).findOne(any(Specification.class));
    }

    @Test
    void testFindOrderByCheckoutId_whenValidCheckoutId_returnsOrder() {
        // Given
        when(orderRepository.findByCheckoutId("checkout123")).thenReturn(Optional.of(testOrder));

        // When
        Order result = orderService.findOrderByCheckoutId("checkout123");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCheckoutId()).isEqualTo("checkout123");
    }

    @Test
    void testGetAllOrder_whenOrdersExist_returnsOrderListVm() {
        // Given
        org.springframework.data.domain.Page<Order> mockPage = new org.springframework.data.domain.PageImpl<>(
            List.of(testOrder),
            org.springframework.data.domain.PageRequest.of(0, 10),
            1
        );
        when(orderRepository.findAll(any(Specification.class), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(mockPage);

        // When
        var result = orderService.getAllOrder(
            org.springframework.data.util.Pair.of(ZonedDateTime.now().minusDays(7), ZonedDateTime.now()),
            null,
            List.of(),
            org.springframework.data.util.Pair.of("", ""),
            null,
            org.springframework.data.util.Pair.of(0, 10)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.orderList()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void testGetAllOrder_whenNoOrders_returnsEmptyList() {
        // Given
        org.springframework.data.domain.Page<Order> emptyPage = new org.springframework.data.domain.PageImpl<>(
            List.of(),
            org.springframework.data.domain.PageRequest.of(0, 10),
            0
        );
        when(orderRepository.findAll(any(Specification.class), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(emptyPage);

        // When
        var result = orderService.getAllOrder(
            org.springframework.data.util.Pair.of(ZonedDateTime.now().minusDays(7), ZonedDateTime.now()),
            "Product",
            List.of(OrderStatus.ACCEPTED),
            org.springframework.data.util.Pair.of("US", "1234567890"),
            "test@example.com",
            org.springframework.data.util.Pair.of(0, 10)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.orderList()).isNull();
        assertThat(result.totalElements()).isEqualTo(0);
        assertThat(result.totalPages()).isEqualTo(0);
    }

    @Test
    void testGetAllOrder_whenFilteredByMultipleCriteria_returnsFilteredOrders() {
        // Given
        org.springframework.data.domain.Page<Order> mockPage = new org.springframework.data.domain.PageImpl<>(
            List.of(testOrder),
            org.springframework.data.domain.PageRequest.of(0, 20),
            1
        );
        when(orderRepository.findAll(any(Specification.class), any(org.springframework.data.domain.Pageable.class)))
            .thenReturn(mockPage);

        // When
        var result = orderService.getAllOrder(
            org.springframework.data.util.Pair.of(ZonedDateTime.now().minusDays(30), ZonedDateTime.now()),
            "TestProduct",
            List.of(OrderStatus.PENDING, OrderStatus.PAID),
            org.springframework.data.util.Pair.of("Canada", "9876543210"),
            "customer@test.com",
            org.springframework.data.util.Pair.of(0, 20)
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.orderList()).hasSize(1);
        verify(orderRepository).findAll(any(Specification.class), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void testUpdateOrderPaymentStatus_whenPaymentNotCompleted_doesNotSetOrderStatusPaid() {
        // Given
        PaymentOrderStatusVm statusVm = PaymentOrderStatusVm.builder()
            .orderId(1L)
            .orderStatus(OrderStatus.PENDING.name())
            .paymentId(100L)
            .paymentStatus(PaymentStatus.PENDING.name())
            .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        PaymentOrderStatusVm result = orderService.updateOrderPaymentStatus(statusVm);

        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();
        assertThat(savedOrder.getPaymentId()).isEqualTo(100L);
        assertThat(savedOrder.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
        // Order status should NOT be set to PAID when payment is not COMPLETED
        assertThat(savedOrder.getOrderStatus()).isNotEqualTo(OrderStatus.PAID);
    }

    @Test
    void testGetMyOrders_whenFilteredByProductName_returnsFilteredOrders() {
        // Mock SecurityContext for authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        
        when(jwt.getSubject()).thenReturn("user456");
        when(authentication.getToken()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(orders);

        // When
        List<OrderGetVm> result = orderService.getMyOrders("ProductName", OrderStatus.ACCEPTED);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(orderRepository).findAll(any(Specification.class), any(Sort.class));
    }
}
