package com.yas.storefrontbff.controller;

import com.yas.storefrontbff.viewmodel.AuthenticationInfoVm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        authenticationController = new AuthenticationController();
    }

    @Test
    void user_whenPrincipalIsNull_returnsUnauthenticated() {
        ResponseEntity<AuthenticationInfoVm> response = authenticationController.user(null);

        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAuthenticated());
        assertNull(response.getBody().authenticatedUser());
    }

    @Test
    void user_whenPrincipalIsPresent_returnsAuthenticatedWithUsername() {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("preferred_username")).thenReturn("testuser");

        ResponseEntity<AuthenticationInfoVm> response = authenticationController.user(principal);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isAuthenticated());
        assertNotNull(response.getBody().authenticatedUser());
        assertEquals("testuser", response.getBody().authenticatedUser().username());
    }
}
