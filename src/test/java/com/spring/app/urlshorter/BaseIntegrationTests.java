package com.spring.app.urlshorter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.app.urlshorter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BaseIntegrationTests.Initializer.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTests {
    protected static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:16");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected List<JpaRepository<?,?>> repositories;

    @BeforeEach
    public void beforeEach() {
        repositories.forEach(CrudRepository::deleteAll);
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
