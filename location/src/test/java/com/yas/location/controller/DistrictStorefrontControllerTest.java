package com.yas.location.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yas.location.service.DistrictService;
import com.yas.location.viewmodel.district.DistrictGetVm;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DistrictStorefrontController.class,
    excludeAutoConfiguration = OAuth2ResourceServerAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class DistrictStorefrontControllerTest {

    @MockitoBean
    private DistrictService districtService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetList_whenCalledWithStorefrontPath_thenReturnOk() throws Exception {
        DistrictGetVm vm = new DistrictGetVm(1L, "District 1");
        when(districtService.getList(1L)).thenReturn(List.of(vm));

        this.mockMvc.perform(get("/storefront/district/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetList_whenCalledWithBackofficePath_thenReturnOk() throws Exception {
        DistrictGetVm vm = new DistrictGetVm(2L, "District 2");
        when(districtService.getList(2L)).thenReturn(List.of(vm));

        this.mockMvc.perform(get("/backoffice/district/2"))
            .andExpect(status().isOk());
    }
}
