package com.yas.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.yas.order.model.csv.OrderItemCsv;
import com.yas.order.model.enumeration.DeliveryMethod;
import com.yas.order.model.enumeration.DeliveryStatus;
import com.yas.order.model.enumeration.OrderStatus;
import com.yas.order.model.enumeration.PaymentStatus;
import com.yas.order.viewmodel.order.OrderBriefVm;
import com.yas.order.viewmodel.orderaddress.OrderAddressVm;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class OrderMapperTest {

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    void testToCsv_whenNormalCase_returnOrderItemCsv() {
        OrderAddressVm billingAddress = new OrderAddressVm(
            1L,
            "John Doe",
            "1234567890",
            "123 Main St",
            "Apt 4",
            "New York",
            "10001",
            1L,
            "District 1",
            1L,
            "State 1",
            1L,
            "USA"
        );

        OrderBriefVm orderBriefVm = OrderBriefVm.builder()
            .id(1L)
            .email("test@example.com")
            .billingAddressVm(billingAddress)
            .totalPrice(BigDecimal.valueOf(100.0))
            .orderStatus(OrderStatus.PENDING)
            .deliveryMethod(DeliveryMethod.VIETTEL_POST)
            .deliveryStatus(DeliveryStatus.PREPARING)
            .paymentStatus(PaymentStatus.PENDING)
            .createdOn(ZonedDateTime.now())
            .build();

        OrderItemCsv result = orderMapper.toCsv(orderBriefVm);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPhone()).isEqualTo("1234567890");
    }

    @Test
    void testToCsv_whenBillingAddressIsNull_returnOrderItemCsvWithNullPhone() {
        OrderBriefVm orderBriefVm = OrderBriefVm.builder()
            .id(1L)
            .email("test@example.com")
            .billingAddressVm(null)
            .totalPrice(BigDecimal.valueOf(100.0))
            .orderStatus(OrderStatus.PENDING)
            .deliveryMethod(DeliveryMethod.VIETTEL_POST)
            .deliveryStatus(DeliveryStatus.PREPARING)
            .paymentStatus(PaymentStatus.PENDING)
            .createdOn(ZonedDateTime.now())
            .build();

        OrderItemCsv result = orderMapper.toCsv(orderBriefVm);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPhone()).isNull();
    }
}
