package io.framework.oh.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MyBatisConfig {

    @Autowired
    @Qualifier("sqlSessionFactoryWithLogging")
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(new IntegerTypeHandler());
        sqlSessionFactory.getConfiguration().getTypeHandlerRegistry().register(new LocalDateTimeTypeHandler());
        registerTypeHandlers();
    }

    public void registerTypeHandlers() {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration().getTypeHandlerRegistry();
        typeHandlerRegistry.register(BooleanTypeHandler.class);
    }

    public static class IntegerTypeHandler implements TypeHandler<Integer> {
        @Override
        public void setParameter(PreparedStatement ps, int i, Integer parameter, JdbcType jdbcType) throws SQLException {
            if (parameter == null) {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } else {
                ps.setInt(i, parameter);
            }
        }

        @Override
        public Integer getResult(ResultSet rs, String columnName) throws SQLException {
            return rs.getInt(columnName);
        }

        @Override
        public Integer getResult(ResultSet rs, int columnIndex) throws SQLException {
            return rs.getInt(columnIndex);
        }

        @Override
        public Integer getResult(CallableStatement cs, int columnIndex) throws SQLException {
            return cs.getInt(columnIndex);
        }
    }

    @MappedTypes(Boolean.class)
    public static class BooleanTypeHandler extends BaseTypeHandler<Boolean> {
        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
            ps.setString(i, parameter ? "Y" : "N");
        }

        @Override
        public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return convertToBoolean(rs.getString(columnName));
        }

        @Override
        public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return convertToBoolean(rs.getString(columnIndex));
        }

        @Override
        public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return convertToBoolean(cs.getString(columnIndex));
        }

        private Boolean convertToBoolean(String value) {
            return "Y".equalsIgnoreCase(value);
        }
    }
} 