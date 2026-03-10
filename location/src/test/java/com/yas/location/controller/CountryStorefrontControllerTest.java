package com.yas.location.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yas.location.service.CountryService;
import com.yas.location.viewmodel.country.CountryVm;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CountryStorefrontController.class,
    excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class CountryStorefrontControllerTest {

    @MockitoBean
    private CountryService countryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testListCountries_whenCalled_thenReturnOk() throws Exception {
        CountryVm countryVm = new CountryVm(1L, "US", "USA", "USA", 
            true, true, true, true, true);
        when(countryService.findAllCountries()).thenReturn(List.of(countryVm));

        this.mockMvc.perform(get("/storefront/countries"))
            .andExpect(status().isOk());
    }
}
