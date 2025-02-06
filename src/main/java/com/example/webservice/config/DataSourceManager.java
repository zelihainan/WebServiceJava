package com.example.webservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceManager {
    private static final Map<String, DataSource> dataSourceCache = new ConcurrentHashMap<>();

    @Primary
    @Bean
    public DataSource defaultDataSource() {
        return null;
    }

    public static DataSource createDataSource(String url, String username, String password, String databaseName) {
        String key = url + databaseName;

        if (dataSourceCache.containsKey(key)) {
            return dataSourceCache.get(key);
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlserver://" + url + ";databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true;");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setMaxLifetime(1800000);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        dataSourceCache.put(key, dataSource);
        return dataSource;
    }
}