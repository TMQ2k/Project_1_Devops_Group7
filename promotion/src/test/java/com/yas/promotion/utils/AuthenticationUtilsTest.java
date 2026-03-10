package com.yas.promotion.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class AuthenticationUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testExtractUserId_whenAnonymousUser_thenThrowAccessDeniedException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        AnonymousAuthenticationToken anonymousAuth = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AccessDeniedException.class, AuthenticationUtils::extractUserId);
    }

    @Test
    void testExtractUserId_whenValidJwtToken_thenReturnSubject() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("user123");
        
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);
        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        SecurityContextHolder.setContext(securityContext);

        String userId = AuthenticationUtils.extractUserId();
        assertEquals("user123", userId);
    }
}
