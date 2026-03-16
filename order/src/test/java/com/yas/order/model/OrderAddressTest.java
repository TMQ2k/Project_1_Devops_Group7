package com.yas.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderAddressTest {

    @Test
    void testOrderAddressBuilder_whenNormalCase_createOrderAddress() {
        OrderAddress orderAddress = OrderAddress.builder()
            .id(1L)
            .contactName("John Doe")
            .phone("1234567890")
            .addressLine1("123 Main St")
            .addressLine2("Apt 4")
            .city("New York")
            .zipCode("10001")
            .districtId(1L)
            .districtName("District 1")
            .stateOrProvinceId(1L)
            .stateOrProvinceName("State 1")
            .countryId(1L)
            .countryName("USA")
            .build();

        assertThat(orderAddress).isNotNull();
        assertThat(orderAddress.getId()).isEqualTo(1L);
        assertThat(orderAddress.getContactName()).isEqualTo("John Doe");
        assertThat(orderAddress.getPhone()).isEqualTo("1234567890");
        assertThat(orderAddress.getAddressLine1()).isEqualTo("123 Main St");
        assertThat(orderAddress.getAddressLine2()).isEqualTo("Apt 4");
        assertThat(orderAddress.getCity()).isEqualTo("New York");
        assertThat(orderAddress.getZipCode()).isEqualTo("10001");
        assertThat(orderAddress.getDistrictId()).isEqualTo(1L);
        assertThat(orderAddress.getDistrictName()).isEqualTo("District 1");
        assertThat(orderAddress.getStateOrProvinceId()).isEqualTo(1L);
        assertThat(orderAddress.getStateOrProvinceName()).isEqualTo("State 1");
        assertThat(orderAddress.getCountryId()).isEqualTo(1L);
        assertThat(orderAddress.getCountryName()).isEqualTo("USA");
    }

    @Test
    void testOrderAddressSetters_whenNormalCase_updateOrderAddress() {
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setId(2L);
        orderAddress.setContactName("Jane Doe");
        orderAddress.setPhone("0987654321");
        orderAddress.setAddressLine1("456 Oak Ave");
        orderAddress.setAddressLine2("Suite 200");
        orderAddress.setCity("Los Angeles");
        orderAddress.setZipCode("90001");
        orderAddress.setDistrictId(2L);
        orderAddress.setDistrictName("District 2");
        orderAddress.setStateOrProvinceId(2L);
        orderAddress.setStateOrProvinceName("State 2");
        orderAddress.setCountryId(2L);
        orderAddress.setCountryName("Canada");

        assertThat(orderAddress.getId()).isEqualTo(2L);
        assertThat(orderAddress.getContactName()).isEqualTo("Jane Doe");
        assertThat(orderAddress.getPhone()).isEqualTo("0987654321");
        assertThat(orderAddress.getAddressLine1()).isEqualTo("456 Oak Ave");
        assertThat(orderAddress.getAddressLine2()).isEqualTo("Suite 200");
        assertThat(orderAddress.getCity()).isEqualTo("Los Angeles");
        assertThat(orderAddress.getZipCode()).isEqualTo("90001");
        assertThat(orderAddress.getDistrictId()).isEqualTo(2L);
        assertThat(orderAddress.getDistrictName()).isEqualTo("District 2");
        assertThat(orderAddress.getStateOrProvinceId()).isEqualTo(2L);
        assertThat(orderAddress.getStateOrProvinceName()).isEqualTo("State 2");
        assertThat(orderAddress.getCountryId()).isEqualTo(2L);
        assertThat(orderAddress.getCountryName()).isEqualTo("Canada");
    }
}
