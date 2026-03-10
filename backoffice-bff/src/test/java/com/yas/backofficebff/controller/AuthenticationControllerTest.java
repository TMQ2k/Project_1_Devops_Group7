package com.yas.backofficebff.controller;

import com.yas.backofficebff.viewmodel.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Test
    void testAuthenticationControllerCanBeInstantiated() {
        AuthenticationController controller = new AuthenticationController();
        assertNotNull(controller);
    }

    @Test
    void testUserMethod_whenPrincipalProvided_returnsAuthenticatedUser() {
        AuthenticationController controller = new AuthenticationController();
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("preferred_username")).thenReturn("testuser");
        
        ResponseEntity<AuthenticatedUser> result = controller.user(principal);
        
        assertNotNull(result);
        assertNotNull(result.getBody());
        assertEquals("testuser", result.getBody().username());
    }
}
