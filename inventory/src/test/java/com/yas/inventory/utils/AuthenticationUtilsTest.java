package com.yas.inventory.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.yas.commonlibrary.exception.AccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
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
    void extractUserId_whenJwtAuth_shouldReturnSubject() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("user-123");
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authToken);
        SecurityContextHolder.setContext(context);

        String result = AuthenticationUtils.extractUserId();

        assertEquals("user-123", result);
    }

    @Test
    void extractUserId_whenAnonymous_shouldThrowAccessDeniedException() {
        AnonymousAuthenticationToken anonymousToken = new AnonymousAuthenticationToken(
            "key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(anonymousToken);
        SecurityContextHolder.setContext(context);

        assertThrows(AccessDeniedException.class, () -> AuthenticationUtils.extractUserId());
    }

    @Test
    void extractJwt_shouldReturnTokenValue() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("my-jwt-token");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(jwt);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        String result = AuthenticationUtils.extractJwt();

        assertNotNull(result);
        assertEquals("my-jwt-token", result);
    }
}
