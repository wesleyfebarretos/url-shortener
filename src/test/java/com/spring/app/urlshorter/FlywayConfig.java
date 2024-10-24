package com.spring.app.urlshorter;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayConfig {
    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    void migrateDb() {
        Flyway.configure().dataSource(dataSource).locations("classpath:db/migration").load().migrate();
    }
}
