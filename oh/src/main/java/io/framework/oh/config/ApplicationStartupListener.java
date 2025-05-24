package io.framework.oh.config;

import io.framework.oh.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupListener {

    private static final String ASCII_ART = """
            _     ______                                           _    
           | |   |  ____|                                         | |   
       ___ | |__ | |__ _ __ __ _ _ __ ___   _____      _____  _ __| | __
      / _ \\| '_ \\|  __| '__/ _` | '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ /
     | (_) | | | | |  | | | (_| | | | | | |  __/\\ V  V / (_) | |  |   < 
      \\___/|_| |_|_|  |_|  \\__,_|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_\\
                                                                        
                                                                        """;

    private final HealthCheckService healthCheckService;
    private final SystemInfoLogger systemInfoLogger;

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted() {
        System.out.println("\n" + ASCII_ART);
        systemInfoLogger.logSystemInfo();
        
        // Perform health checks
        boolean dbConnection = healthCheckService.checkDatabaseConnection();
        int dataCount = healthCheckService.getDataCount();
        
        log.info("Health Check Results:");
        log.info("Database Connection: {}", dbConnection ? "OK" : "FAILED");
        log.info("Sample Data Count: {}", dataCount);
        
        if (dbConnection) {
            log.info("ohFramework has started successfully!");
        } else {
            log.warn("ohFramework started with some issues. Please check the logs for details.");
        }
    }
} 