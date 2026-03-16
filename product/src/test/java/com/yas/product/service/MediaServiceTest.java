package com.yas.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.yas.commonlibrary.config.ServiceUrlConfig;
import com.yas.product.viewmodel.NoFileMediaVm;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MediaServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ServiceUrlConfig serviceUrlConfig;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    private MediaService mediaService;

    @BeforeEach
    void setUp() {
        mediaService = new MediaService(restClient, serviceUrlConfig);
    }

    @Test
    void getMedia_nullId_returnsDefaultNoFileMediaVm() {
        NoFileMediaVm result = mediaService.getMedia(null);
        assertNotNull(result);
        assertNull(result.id());
        assertEquals("", result.caption());
        assertEquals("", result.fileName());
        assertEquals("", result.mediaType());
        assertEquals("", result.url());
    }

    @Test
    void getMedia_withId_returnsMediaVm() {
        when(serviceUrlConfig.media()).thenReturn("http://localhost:8080");

        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        NoFileMediaVm expected = new NoFileMediaVm(1L, "caption", "file.jpg", "image/jpeg", "http://url");

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(NoFileMediaVm.class)).thenReturn(expected);

        NoFileMediaVm result = mediaService.getMedia(1L);
        assertEquals(expected, result);
    }

    @Test
    void removeMedia_callsRestClient() {
        when(serviceUrlConfig.media()).thenReturn("http://localhost:8080");
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn("test-token");

        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class, withSettings().lenient());
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(restClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Void.class)).thenReturn(null);

        assertDoesNotThrow(() -> mediaService.removeMedia(1L));

        SecurityContextHolder.clearContext();
    }

    @Test
    void handleMediaFallback_throwsException() throws Throwable {
        RuntimeException ex = new RuntimeException("Connection failed");
        assertThrows(RuntimeException.class, () -> {
            // Test through the abstract handler
            mediaService.handleBodilessFallback(ex);
        });
    }
}
