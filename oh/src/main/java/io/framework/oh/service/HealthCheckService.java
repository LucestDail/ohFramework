package io.framework.oh.service;

import io.framework.oh.model.NewsCompany;
import java.util.List;

public interface HealthCheckService {
    boolean checkDatabaseConnection();
    List<NewsCompany> getSampleData();
    int getDataCount();
} 