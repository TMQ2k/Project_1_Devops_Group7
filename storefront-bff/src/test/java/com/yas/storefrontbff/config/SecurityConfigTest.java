package com.yas.storefrontbff.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        ReactiveClientRegistrationRepository repo = mock(ReactiveClientRegistrationRepository.class);
        securityConfig = new SecurityConfig(repo);
    }

    @Test
    void generateAuthoritiesFromClaim_mapsRolesToGrantedAuthorities() {
        Collection<GrantedAuthority> authorities =
                securityConfig.generateAuthoritiesFromClaim(List.of("admin", "user"));

        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_user")));
    }

    @Test
    void userAuthoritiesMapper_whenOidcWithRealmAccess_mapsRoles() {
        Map<String, Object> claims = Map.of(
                "sub", "user-id",
                "realm_access", Map.of("roles", List.of("admin"))
        );
        OidcIdToken idToken = new OidcIdToken(
                "token-value", Instant.now(), Instant.now().plusSeconds(60), claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        OidcUserAuthority oidcAuthority = new OidcUserAuthority(idToken, userInfo);

        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();
        Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(Set.of(oidcAuthority));

        assertTrue(result.stream().anyMatch(a -> a.getAuthority().equals("ROLE_admin")));
    }

    @Test
    void userAuthoritiesMapper_whenOidcWithoutRealmAccess_returnsEmpty() {
        Map<String, Object> claims = Map.of("sub", "user-id");
        OidcIdToken idToken = new OidcIdToken(
                "token-value", Instant.now(), Instant.now().plusSeconds(60), claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        OidcUserAuthority oidcAuthority = new OidcUserAuthority(idToken, userInfo);

        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();
        Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(Set.of(oidcAuthority));

        assertTrue(result.isEmpty());
    }

    @Test
    void userAuthoritiesMapper_whenOAuth2WithRealmAccess_mapsRoles() {
        Map<String, Object> attributes = Map.of(
                "realm_access", Map.of("roles", List.of("user"))
        );
        OAuth2UserAuthority oauth2Authority = new OAuth2UserAuthority(attributes);

        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();
        Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(Set.of(oauth2Authority));

        assertTrue(result.stream().anyMatch(a -> a.getAuthority().equals("ROLE_user")));
    }

    @Test
    void userAuthoritiesMapper_whenOAuth2WithoutRealmAccess_returnsEmpty() {
        Map<String, Object> attributes = Map.of("sub", "user-id");
        OAuth2UserAuthority oauth2Authority = new OAuth2UserAuthority(attributes);

        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();
        Collection<? extends GrantedAuthority> result = mapper.mapAuthorities(Set.of(oauth2Authority));

        assertTrue(result.isEmpty());
    }
}
