package com.yas.order.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    void testErrorCodeConstants() {
        assertThat(Constants.ErrorCode.ORDER_NOT_FOUND).isEqualTo("ORDER_NOT_FOUND");
        assertThat(Constants.ErrorCode.CHECKOUT_NOT_FOUND).isEqualTo("CHECKOUT_NOT_FOUND");
        assertThat(Constants.ErrorCode.CHECKOUT_ITEM_NOT_EMPTY).isEqualTo("CHECKOUT_ITEM_NOT_EMPTY");
        assertThat(Constants.ErrorCode.SIGN_IN_REQUIRED).isEqualTo("SIGN_IN_REQUIRED");
    }

    @Test
    void testMessageCodeConstants() {
        assertThat(Constants.MessageCode.CREATE_CHECKOUT).isEqualTo("Create checkout {} by user {}");
        assertThat(Constants.MessageCode.UPDATE_CHECKOUT_STATUS).isEqualTo("Update checkout {} STATUS from {} to {}");
        assertThat(Constants.MessageCode.UPDATE_CHECKOUT_PAYMENT).isEqualTo("Update checkout {} PAYMENT from {} to {}");
    }

    @Test
    void testColumnConstants() {
        assertThat(Constants.Column.ID_COLUMN).isEqualTo("id");
        assertThat(Constants.Column.CREATE_ON_COLUMN).isEqualTo("createdOn");
        assertThat(Constants.Column.CREATE_BY_COLUMN).isEqualTo("createdBy");
        assertThat(Constants.Column.ORDER_EMAIL_COLUMN).isEqualTo("email");
        assertThat(Constants.Column.ORDER_PHONE_COLUMN).isEqualTo("phone");
        assertThat(Constants.Column.ORDER_ORDER_ID_COLUMN).isEqualTo("orderId");
        assertThat(Constants.Column.ORDER_ORDER_STATUS_COLUMN).isEqualTo("orderStatus");
        assertThat(Constants.Column.ORDER_COUNTRY_NAME_COLUMN).isEqualTo("countryName");
        assertThat(Constants.Column.ORDER_SHIPPING_ADDRESS_ID_COLUMN).isEqualTo("shippingAddressId");
        assertThat(Constants.Column.ORDER_BILLING_ADDRESS_ID_COLUMN).isEqualTo("billingAddressId");
        assertThat(Constants.Column.ORDER_ITEM_PRODUCT_ID_COLUMN).isEqualTo("productId");
        assertThat(Constants.Column.ORDER_ITEM_PRODUCT_NAME_COLUMN).isEqualTo("productName");
    }
}
