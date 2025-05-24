package io.framework.oh.config;

import io.framework.oh.service.HealthCheckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationStartedEvent;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationStartupListenerTest {

    @Mock
    private HealthCheckService healthCheckService;

    @Mock
    private ApplicationStartedEvent applicationStartedEvent;

    @InjectMocks
    private ApplicationStartupListener applicationStartupListener;

    @Test
    void onApplicationStarted_ShouldLogSuccess_WhenDatabaseConnectionIsHealthy() {
        when(healthCheckService.checkDatabaseConnection()).thenReturn(true);
        when(healthCheckService.getDataCount()).thenReturn(5);

        applicationStartupListener.onApplicationStarted();

        verify(healthCheckService).checkDatabaseConnection();
        verify(healthCheckService).getDataCount();
    }

    @Test
    void onApplicationStarted_ShouldLogWarning_WhenDatabaseConnectionIsUnhealthy() {
        when(healthCheckService.checkDatabaseConnection()).thenReturn(false);

        applicationStartupListener.onApplicationStarted();

        verify(healthCheckService).checkDatabaseConnection();
        verify(healthCheckService, never()).getDataCount();
    }

    @Test
    void onApplicationStarted_ShouldHandleException_WhenHealthCheckFails() {
        when(healthCheckService.checkDatabaseConnection()).thenThrow(new RuntimeException("Database error"));

        applicationStartupListener.onApplicationStarted();

        verify(healthCheckService).checkDatabaseConnection();
        verify(healthCheckService, never()).getDataCount();
    }
} 