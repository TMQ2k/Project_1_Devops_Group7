package com.yas.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.AccessDeniedException;
import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.customer.model.UserAddress;
import com.yas.customer.repository.UserAddressRepository;
import com.yas.customer.viewmodel.address.ActiveAddressVm;
import com.yas.customer.viewmodel.address.AddressDetailVm;
import com.yas.customer.viewmodel.address.AddressPostVm;
import com.yas.customer.viewmodel.address.AddressVm;
import com.yas.customer.viewmodel.useraddress.UserAddressVm;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private UserAddressService userAddressService;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user123", null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetUserAddressList_whenAnonymousUser_thenThrowAccessDeniedException() {
        Authentication anonymousAuth = new UsernamePasswordAuthenticationToken("anonymousUser", null);
        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);

        assertThrows(AccessDeniedException.class, () -> userAddressService.getUserAddressList());
    }

    @Test
    void testGetUserAddressList_whenValidUser_thenReturnAddressList() {
        UserAddress userAddress1 = UserAddress.builder()
            .id(1L)
            .userId("user123")
            .addressId(10L)
            .isActive(true)
            .build();
        
        UserAddress userAddress2 = UserAddress.builder()
            .id(2L)
            .userId("user123")
            .addressId(20L)
            .isActive(false)
            .build();

        AddressDetailVm addressDetail1 = new AddressDetailVm(
            10L, "John Doe", "123456789", "123 Street", "City", "12345",
            1L, "District1", 1L, "State1", 1L, "Country1"
        );

        AddressDetailVm addressDetail2 = new AddressDetailVm(
            20L, "Jane Doe", "987654321", "456 Avenue", "Town", "54321",
            2L, "District2", 2L, "State2", 2L, "Country2"
        );

        when(userAddressRepository.findAllByUserId("user123"))
            .thenReturn(List.of(userAddress1, userAddress2));
        when(locationService.getAddressesByIdList(anyList()))
            .thenReturn(List.of(addressDetail1, addressDetail2));

        List<ActiveAddressVm> result = userAddressService.getUserAddressList();

        assertEquals(2, result.size());
        // Active address should be first (sorted by isActive reversed)
        assertTrue(result.get(0).isActive());
        assertFalse(result.get(1).isActive());
        assertEquals("John Doe", result.get(0).contactName());
    }

    @Test
    void testGetAddressDefault_whenAnonymousUser_thenThrowAccessDeniedException() {
        Authentication anonymousAuth = new UsernamePasswordAuthenticationToken("anonymousUser", null);
        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);

        assertThrows(AccessDeniedException.class, () -> userAddressService.getAddressDefault());
    }

    @Test
    void testGetAddressDefault_whenNoActiveAddress_thenThrowNotFoundException() {
        when(userAddressRepository.findByUserIdAndIsActiveTrue("user123"))
            .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userAddressService.getAddressDefault());
    }

    @Test
    void testGetAddressDefault_whenActiveAddressExists_thenReturnAddress() {
        UserAddress userAddress = UserAddress.builder()
            .id(1L)
            .userId("user123")
            .addressId(10L)
            .isActive(true)
            .build();

        AddressDetailVm addressDetail = new AddressDetailVm(
            10L, "John Doe", "123456789", "123 Street", "City", "12345",
            1L, "District1", 1L, "State1", 1L, "Country1"
        );

        when(userAddressRepository.findByUserIdAndIsActiveTrue("user123"))
            .thenReturn(Optional.of(userAddress));
        when(locationService.getAddressById(10L)).thenReturn(addressDetail);

        AddressDetailVm result = userAddressService.getAddressDefault();

        assertEquals(10L, result.id());
        assertEquals("John Doe", result.contactName());
    }

    @Test
    void testCreateAddress_whenFirstAddress_thenSetAsActive() {
        AddressPostVm addressPostVm = new AddressPostVm(
            "John Doe", "123456789", "123 Street", "City", "12345",
            1L, 1L, 1L
        );

        AddressVm addressVm = AddressVm.builder()
            .id(10L)
            .contactName("John Doe")
            .phone("123456789")
            .addressLine1("123 Street")
            .city("City")
            .zipCode("12345")
            .districtId(1L)
            .stateOrProvinceId(1L)
            .countryId(1L)
            .build();

        when(userAddressRepository.findAllByUserId("user123")).thenReturn(List.of());
        when(locationService.createAddress(addressPostVm)).thenReturn(addressVm);
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> {
            UserAddress ua = invocation.getArgument(0);
            ua.setId(1L);
            return ua;
        });

        UserAddressVm result = userAddressService.createAddress(addressPostVm);

        assertEquals(1L, result.id());
        assertEquals(10L, result.addressGetVm().id());
        verify(userAddressRepository).save(any(UserAddress.class));
    }

    @Test
    void testCreateAddress_whenNotFirstAddress_thenSetAsInactive() {
        AddressPostVm addressPostVm = new AddressPostVm(
            "Jane Doe", "987654321", "456 Avenue", "Town", "54321",
            2L, 2L, 2L
        );

        AddressVm addressVm = AddressVm.builder()
            .id(20L)
            .contactName("Jane Doe")
            .phone("987654321")
            .addressLine1("456 Avenue")
            .city("Town")
            .zipCode("54321")
            .districtId(2L)
            .stateOrProvinceId(2L)
            .countryId(2L)
            .build();

        UserAddress existingAddress = UserAddress.builder()
            .id(1L)
            .userId("user123")
            .addressId(10L)
            .isActive(true)
            .build();

        when(userAddressRepository.findAllByUserId("user123"))
            .thenReturn(List.of(existingAddress));
        when(locationService.createAddress(addressPostVm)).thenReturn(addressVm);
        when(userAddressRepository.save(any(UserAddress.class))).thenAnswer(invocation -> {
            UserAddress ua = invocation.getArgument(0);
            ua.setId(2L);
            return ua;
        });

        UserAddressVm result = userAddressService.createAddress(addressPostVm);

        assertEquals(2L, result.id());
        assertEquals(20L, result.addressGetVm().id());
    }

    @Test
    void testDeleteAddress_whenAddressNotFound_thenThrowNotFoundException() {
        when(userAddressRepository.findOneByUserIdAndAddressId("user123", 10L))
            .thenReturn(null);

        assertThrows(NotFoundException.class, () -> userAddressService.deleteAddress(10L));
    }

    @Test
    void testDeleteAddress_whenAddressExists_thenDeleteIt() {
        UserAddress userAddress = UserAddress.builder()
            .id(1L)
            .userId("user123")
            .addressId(10L)
            .isActive(true)
            .build();

        when(userAddressRepository.findOneByUserIdAndAddressId("user123", 10L))
            .thenReturn(userAddress);

        userAddressService.deleteAddress(10L);

        verify(userAddressRepository).delete(userAddress);
    }

    @Test
    void testChooseDefaultAddress_whenAddressSelected_thenSetAsActive() {
        UserAddress address1 = UserAddress.builder()
            .id(1L)
            .userId("user123")
            .addressId(10L)
            .isActive(true)
            .build();

        UserAddress address2 = UserAddress.builder()
            .id(2L)
            .userId("user123")
            .addressId(20L)
            .isActive(false)
            .build();

        when(userAddressRepository.findAllByUserId("user123"))
            .thenReturn(List.of(address1, address2));

        userAddressService.chooseDefaultAddress(20L);

        assertFalse(address1.getIsActive());
        assertTrue(address2.getIsActive());
        verify(userAddressRepository).saveAll(anyList());
    }
}
