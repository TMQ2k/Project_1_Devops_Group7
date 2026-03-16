package com.yas.storefrontbff.viewmodel;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewModelTest {

    @Test
    void tokenResponseVm_holdsValues() {
        TokenResponseVm vm = new TokenResponseVm("access", "refresh");
        assertEquals("access", vm.accessToken());
        assertEquals("refresh", vm.refreshToken());
    }

    @Test
    void cartDetailVm_holdsValues() {
        CartDetailVm vm = new CartDetailVm(1L, 2L, 3);
        assertEquals(1L, vm.id());
        assertEquals(2L, vm.productId());
        assertEquals(3, vm.quantity());
    }

    @Test
    void cartItemVm_fromCartDetailVm_mapsCorrectly() {
        CartDetailVm detail = new CartDetailVm(1L, 10L, 5);
        CartItemVm item = CartItemVm.fromCartDetailVm(detail);
        assertEquals(10L, item.productId());
        assertEquals(5, item.quantity());
    }

    @Test
    void guestUserVm_holdsValues() {
        GuestUserVm vm = new GuestUserVm("uid", "a@b.com", "pass");
        assertEquals("uid", vm.userId());
        assertEquals("a@b.com", vm.email());
        assertEquals("pass", vm.password());
    }

    @Test
    void cartGetDetailVm_holdsValues() {
        CartDetailVm detail = new CartDetailVm(1L, 2L, 3);
        CartGetDetailVm vm = new CartGetDetailVm(1L, "cust1", List.of(detail));
        assertEquals(1L, vm.id());
        assertEquals("cust1", vm.customerId());
        assertEquals(1, vm.cartDetails().size());
    }
}
