package com.yas.backofficebff.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @BeforeEach
    void setUp() {
        clientRegistrationRepository = mock(ReactiveClientRegistrationRepository.class);
        securityConfig = new SecurityConfig(clientRegistrationRepository);
    }

    @Test
    void testUserAuthoritiesMapperForKeycloak_whenOidcWithRealmAccess_returnMappedAuthorities() {
        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();

        Map<String, Object> realmAccess = Map.of("roles", List.of("ADMIN", "USER"));
        Map<String, Object> claims = Map.of("realm_access", realmAccess, "sub", "user123");
        
        OidcIdToken idToken = new OidcIdToken("token", null, null, claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        
        OidcUserAuthority authority = new OidcUserAuthority(idToken, userInfo);
        Collection<? extends GrantedAuthority> authorities = List.of(authority);

        Collection<? extends GrantedAuthority> mappedAuthorities = mapper.mapAuthorities(authorities);

        assertThat(mappedAuthorities).isNotNull();
        assertThat(mappedAuthorities).hasSize(2);
        assertThat(mappedAuthorities.stream().map(GrantedAuthority::getAuthority).toList())
            .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void testUserAuthoritiesMapperForKeycloak_whenOAuth2WithRealmAccess_returnMappedAuthorities() {
        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();

        Map<String, Object> realmAccess = Map.of("roles", List.of("MODERATOR"));
        Map<String, Object> attributes = Map.of("realm_access", realmAccess, "name", "Test User");
        
        OAuth2UserAuthority authority = new OAuth2UserAuthority(attributes);
        Collection<? extends GrantedAuthority> authorities = List.of(authority);

        Collection<? extends GrantedAuthority> mappedAuthorities = mapper.mapAuthorities(authorities);

        assertThat(mappedAuthorities).isNotNull();
        assertThat(mappedAuthorities).hasSize(1);
        assertThat(mappedAuthorities.stream().map(GrantedAuthority::getAuthority).toList())
            .containsExactlyInAnyOrder("ROLE_MODERATOR");
    }

    @Test
    void testUserAuthoritiesMapperForKeycloak_whenNoRealmAccess_returnEmptyAuthorities() {
        GrantedAuthoritiesMapper mapper = securityConfig.userAuthoritiesMapperForKeycloak();

        Map<String, Object> claims = Map.of("sub", "user123");
        OidcIdToken idToken = new OidcIdToken("token", null, null, claims);
        OidcUserInfo userInfo = new OidcUserInfo(claims);
        
        OidcUserAuthority authority = new OidcUserAuthority(idToken, userInfo);
        Collection<? extends GrantedAuthority> authorities = List.of(authority);

        Collection<? extends GrantedAuthority> mappedAuthorities = mapper.mapAuthorities(authorities);

        assertThat(mappedAuthorities).isNotNull();
        assertThat(mappedAuthorities).isEmpty();
    }

    @Test
    void testGenerateAuthoritiesFromClaim_whenRolesProvided_returnGrantedAuthorities() {
        Collection<String> roles = List.of("ADMIN", "USER", "MANAGER");

        Collection<GrantedAuthority> authorities = securityConfig.generateAuthoritiesFromClaim(roles);

        assertThat(authorities).isNotNull();
        assertThat(authorities).hasSize(3);
        assertThat(authorities.stream().map(GrantedAuthority::getAuthority).toList())
            .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER");
    }

    @Test
    void testGenerateAuthoritiesFromClaim_whenEmptyRoles_returnEmptyAuthorities() {
        Collection<String> roles = List.of();

        Collection<GrantedAuthority> authorities = securityConfig.generateAuthoritiesFromClaim(roles);

        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }
}
