package com.example.webservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Dotenv dotenv = Dotenv.load();

    @Primary
    @Bean
    public DataSource defaultDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dotenv.get("DB_URL"));
        hikariConfig.setUsername(dotenv.get("DB_USERNAME"));
        hikariConfig.setPassword(dotenv.get("DB_PASSWORD"));
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTimeout(20000);

        return new HikariDataSource(hikariConfig);
    }
}
