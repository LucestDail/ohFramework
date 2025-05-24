package io.framework.oh.config;

import io.framework.oh.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemInfoLogger {
    private final Environment environment;
    private final HealthCheckService healthCheckService;
    private final BuildProperties buildProperties;

    @Value("${server.port:8080}")
    private String serverPort;

    public void logSystemInfo() {
        try {
            StringBuilder info = new StringBuilder("\n");
            info.append("=".repeat(80)).append("\n");
            info.append("Application Started Successfully\n");
            info.append("=".repeat(80)).append("\n\n");

            // Application Info
            info.append("Application Information:\n");
            info.append("-".repeat(40)).append("\n");
            info.append("Name: ").append(buildProperties.getName()).append("\n");
            info.append("Version: ").append(buildProperties.getVersion()).append("\n");
            info.append("Build Time: ").append(buildProperties.getTime()).append("\n");
            info.append("Active Profiles: ").append(Arrays.toString(environment.getActiveProfiles())).append("\n\n");

            // System Info
            info.append("System Information:\n");
            info.append("-".repeat(40)).append("\n");
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            info.append("OS: ").append(osBean.getName()).append(" ").append(osBean.getVersion()).append("\n");
            info.append("Architecture: ").append(osBean.getArch()).append("\n");
            info.append("Available Processors: ").append(osBean.getAvailableProcessors()).append("\n");
            info.append("System Load Average: ").append(osBean.getSystemLoadAverage()).append("\n\n");

            // Memory Info
            info.append("Memory Information:\n");
            info.append("-".repeat(40)).append("\n");
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            info.append("Heap Memory Usage: ").append(memoryBean.getHeapMemoryUsage().getUsed() / 1024 / 1024).append("MB\n");
            info.append("Non-Heap Memory Usage: ").append(memoryBean.getNonHeapMemoryUsage().getUsed() / 1024 / 1024).append("MB\n\n");

            // Network Info
            info.append("Network Information:\n");
            info.append("-".repeat(40)).append("\n");
            info.append("Host Address: ").append(InetAddress.getLocalHost().getHostAddress()).append("\n");
            info.append("Host Name: ").append(InetAddress.getLocalHost().getHostName()).append("\n");
            info.append("Server Port: ").append(serverPort).append("\n\n");

            // Database Info
            info.append("Database Information:\n");
            info.append("-".repeat(40)).append("\n");
            info.append("Driver: ").append(environment.getProperty("spring.datasource.driver-class-name")).append("\n");
            info.append("URL: ").append(environment.getProperty("spring.datasource.jdbc-url")).append("\n");
            info.append("Username: ").append(environment.getProperty("spring.datasource.username")).append("\n");
            info.append("Schema Location: ").append(environment.getProperty("spring.sql.init.schema-locations")).append("\n");
            info.append("Data Location: ").append(environment.getProperty("spring.sql.init.data-locations")).append("\n\n");

            // Database Health Check
            info.append("Database Health Check:\n");
            info.append("-".repeat(40)).append("\n");
            boolean dbStatus = healthCheckService.checkDatabaseConnection();
            info.append("Status: ").append(dbStatus ? "HEALTHY" : "UNHEALTHY").append("\n");
            info.append("\n");

            // API Endpoints
            info.append("Available API Endpoints:\n");
            info.append("-".repeat(40)).append("\n");
            
            // Health Check Endpoints
            info.append("Health Check Endpoints:\n");
            List<String> healthEndpoints = Arrays.asList(
                "GET  /health/db     - Database health check",
                "GET  /health/sample - Get sample data",
                "GET  /health/count  - Get data count"
            );
            info.append(healthEndpoints.stream().collect(Collectors.joining("\n"))).append("\n\n");

            // Code Management Endpoints
            info.append("Code Management Endpoints:\n");
            List<String> codeEndpoints = Arrays.asList(
                "GET    /api/codes/test           - Test code management endpoints",
                "GET    /api/codes/groups          - Get all code groups",
                "GET    /api/codes/groups/{id}     - Get code group by ID",
                "POST   /api/codes/groups          - Create code group",
                "PUT    /api/codes/groups/{id}     - Update code group",
                "DELETE /api/codes/groups/{id}     - Delete code group",
                "GET    /api/codes                 - Get all codes",
                "GET    /api/codes/group/{id}      - Get codes by group ID",
                "GET    /api/codes/parent/{id}     - Get child codes",
                "GET    /api/codes/{id}            - Get code by ID",
                "POST   /api/codes                 - Create code",
                "PUT    /api/codes/{id}            - Update code",
                "DELETE /api/codes/{id}            - Delete code"
            );
            info.append(codeEndpoints.stream().collect(Collectors.joining("\n"))).append("\n\n");

            // Communication Endpoints
            info.append("Communication Endpoints:\n");
            List<String> commEndpoints = Arrays.asList(
                "GET  /sse/connect   - SSE connection endpoint",
                "WS   /ws            - WebSocket endpoint"
            );
            info.append(commEndpoints.stream().collect(Collectors.joining("\n"))).append("\n");

            info.append("=".repeat(80)).append("\n");

            log.info(info.toString());
        } catch (Exception e) {
            log.error("Error logging system information", e);
        }
    }
} 