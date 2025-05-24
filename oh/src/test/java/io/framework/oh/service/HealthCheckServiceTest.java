package io.framework.oh.service;

import io.framework.oh.mapper.HealthCheckMapper;
import io.framework.oh.model.NewsCompany;
import io.framework.oh.service.impl.HealthCheckServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthCheckServiceTest {

    @Mock
    private HealthCheckMapper healthCheckMapper;

    @InjectMocks
    private HealthCheckServiceImpl healthCheckService;

    @Test
    void checkDatabaseConnection_ShouldReturnTrue_WhenConnectionIsSuccessful() {
        when(healthCheckMapper.countNewsCompanies()).thenReturn(5);

        boolean result = healthCheckService.checkDatabaseConnection();

        assertTrue(result);
        verify(healthCheckMapper).countNewsCompanies();
    }

    @Test
    void checkDatabaseConnection_ShouldReturnFalse_WhenExceptionOccurs() {
        when(healthCheckMapper.countNewsCompanies()).thenThrow(new RuntimeException("Database error"));

        boolean result = healthCheckService.checkDatabaseConnection();

        assertFalse(result);
        verify(healthCheckMapper).countNewsCompanies();
    }

    @Test
    void getSampleData_ShouldReturnData_WhenSuccessful() {
        List<NewsCompany> expectedData = List.of(
            NewsCompany.builder()
                .id(1L)
                .newsCompanyCode("TEST001")
                .newsCompanyName("Test Company")
                .build()
        );
        when(healthCheckMapper.selectNewsCompanies()).thenReturn(expectedData);

        List<NewsCompany> result = healthCheckService.getSampleData();

        assertNotNull(result);
        assertEquals(expectedData.size(), result.size());
        assertEquals(expectedData.get(0).getNewsCompanyCode(), result.get(0).getNewsCompanyCode());
        verify(healthCheckMapper).selectNewsCompanies();
    }

    @Test
    void getDataCount_ShouldReturnCount_WhenSuccessful() {
        when(healthCheckMapper.countNewsCompanies()).thenReturn(10);

        int result = healthCheckService.getDataCount();

        assertEquals(10, result);
        verify(healthCheckMapper).countNewsCompanies();
    }
} 