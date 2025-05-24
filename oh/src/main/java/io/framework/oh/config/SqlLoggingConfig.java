package io.framework.oh.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class SqlLoggingConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactoryWithLogging() {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        configuration.addInterceptor(new SqlLoggingInterceptor());
        return sqlSessionFactory;
    }

    @Intercepts({
        @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class}
        )
    })
    public static class SqlLoggingInterceptor implements Interceptor {
        private static final AtomicInteger queryCount = new AtomicInteger(0);

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            long startTime = System.currentTimeMillis();
            Object result = invocation.proceed();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String sql = formatSql(boundSql.getSql(), parameter, boundSql.getParameterMappings(), mappedStatement.getConfiguration());

            int count = queryCount.incrementAndGet();
            log.info("\n=== Query #{} ===\n{}\nExecution Time: {}ms\nResult: {}\n==================",
                count, sql, executionTime, getResultInfo(result));

            return result;
        }

        private String formatSql(String sql, Object parameter, List<ParameterMapping> parameterMappings, Configuration configuration) {
            if (sql == null || sql.isEmpty()) {
                return "";
            }

            sql = sql.replaceAll("[\\s\n ]+", " ");
            sql = sql.replaceAll("(?i)\\b(select|from|where|and|or|order by|group by|having|insert into|update|delete from|values|set)\\b", "\n$1");
            sql = sql.replaceAll("(?i)\\b(inner join|left join|right join|outer join)\\b", "\n$1");
            sql = sql.replaceAll("(?i)\\b(on|using)\\b", "\n  $1");

            return sql;
        }

        private String getResultInfo(Object result) {
            if (result instanceof List) {
                return String.format("Returned %d rows", ((List<?>) result).size());
            } else if (result instanceof Integer) {
                return String.format("Affected %d rows", result);
            }
            return "Success";
        }

        @Override
        public Object plugin(Object target) {
            return Plugin.wrap(target, this);
        }

        @Override
        public void setProperties(Properties properties) {
            // 필요한 경우 속성 설정
        }
    }
} 