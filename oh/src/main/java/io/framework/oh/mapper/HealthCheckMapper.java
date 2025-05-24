package io.framework.oh.mapper;

import io.framework.oh.model.NewsCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HealthCheckMapper {
    List<NewsCompany> selectNewsCompanies();
    int countNewsCompanies();
    String getTableName();
    void setTableName(@Param("tableName") String tableName);
} 