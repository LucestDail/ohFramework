package io.framework.oh.service.impl;

import io.framework.oh.model.NewsCompany;
import io.framework.oh.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthCheckServiceImpl implements HealthCheckService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean checkDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.info("Database connection check successful");
            return true;
        } catch (Exception e) {
            log.error("Database connection check failed", e);
            return false;
        }
    }

    @Override
    public List<NewsCompany> getSampleData() {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM bs4news_news_company LIMIT 10",
                (rs, rowNum) -> NewsCompany.builder()
                    .id(rs.getLong("id"))
                    .newsCompanyCode(rs.getString("news_company_code"))
                    .build()
            );
        } catch (Exception e) {
            log.warn("Failed to get sample data: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public int getDataCount() {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM bs4news_news_company",
                Integer.class
            );
        } catch (Exception e) {
            log.warn("Failed to get data count: {}", e.getMessage());
            return 0;
        }
    }
} 