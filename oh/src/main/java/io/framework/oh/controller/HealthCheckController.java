package io.framework.oh.controller;

import io.framework.oh.model.NewsCompany;
import io.framework.oh.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/db")
    public ResponseEntity<Boolean> checkDatabaseConnection() {
        log.debug("Received database health check request");
        boolean isHealthy = healthCheckService.checkDatabaseConnection();
        return ResponseEntity.ok(isHealthy);
    }

    @GetMapping("/sample")
    public ResponseEntity<List<NewsCompany>> getSampleData() {
        log.debug("Received sample data request");
        List<NewsCompany> data = healthCheckService.getSampleData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getDataCount() {
        log.debug("Received data count request");
        int count = healthCheckService.getDataCount();
        return ResponseEntity.ok(count);
    }
} 