package com.spring.app.urlshorter;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BaseIntegrationTests.Initializer.class})
@AutoConfigureMockMvc
public abstract class BaseIntegrationTests {
    protected static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:16");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            POSTGRES.start();

            //  Spring way to override enviroments in test run time
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES.getUsername(),
                    "spring.datasource.password=" + POSTGRES.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());

            //  Java native way to override enviroments in test run time
//            System.setProperty("spring.datasource.url", POSTGRES.getJdbcUrl());
//            System.setProperty("spring.datasource.username", POSTGRES.getUsername());
//            System.setProperty("spring.datasource.password", POSTGRES.getPassword());
        }
    }
}
