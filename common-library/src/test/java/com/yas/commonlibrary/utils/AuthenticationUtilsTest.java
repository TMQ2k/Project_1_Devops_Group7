package com.yas.commonlibrary.utils;

import com.yas.commonlibrary.exception.AccessDeniedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationUtilsTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testExtractUserId_whenAnonymousAuthentication_thenThrowAccessDeniedException() {
        SecurityContext securityContext = mock(SecurityContext.class);
        AnonymousAuthenticationToken anonymousAuth =
                new AnonymousAuthenticationToken("key", "anonymous",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        when(securityContext.getAuthentication()).thenReturn(anonymousAuth);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AccessDeniedException.class, AuthenticationUtils::extractUserId);
    }

    @Test
    void testExtractUserId_whenJwtAuthentication_thenReturnUserId() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "none"), Map.of("sub", "user123"));
        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt);

        when(securityContext.getAuthentication()).thenReturn(jwtAuth);
        SecurityContextHolder.setContext(securityContext);

        String userId = AuthenticationUtils.extractUserId();
        assertEquals("user123", userId);
    }
}
