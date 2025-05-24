package io.framework.oh.controller;

import io.framework.oh.model.NewsCompany;
import io.framework.oh.service.HealthCheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckService healthCheckService;

    @Test
    void checkDatabaseConnection_ShouldReturnTrue_WhenConnectionIsHealthy() throws Exception {
        when(healthCheckService.checkDatabaseConnection()).thenReturn(true);

        mockMvc.perform(get("/health/db"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void checkDatabaseConnection_ShouldReturnFalse_WhenConnectionIsUnhealthy() throws Exception {
        when(healthCheckService.checkDatabaseConnection()).thenReturn(false);

        mockMvc.perform(get("/health/db"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void getSampleData_ShouldReturnSampleData() throws Exception {
        List<NewsCompany> sampleData = List.of(
            NewsCompany.builder()
                .id(1L)
                .newsCompanyCode("TEST001")
                .newsCompanyName("Test Company 1")
                .build(),
            NewsCompany.builder()
                .id(2L)
                .newsCompanyCode("TEST002")
                .newsCompanyName("Test Company 2")
                .build()
        );
        when(healthCheckService.getSampleData()).thenReturn(sampleData);

        mockMvc.perform(get("/health/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].newsCompanyCode").value("TEST001"))
                .andExpect(jsonPath("$[1].newsCompanyCode").value("TEST002"));
    }

    @Test
    void getDataCount_ShouldReturnCount() throws Exception {
        when(healthCheckService.getDataCount()).thenReturn(5);

        mockMvc.perform(get("/health/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }
} 