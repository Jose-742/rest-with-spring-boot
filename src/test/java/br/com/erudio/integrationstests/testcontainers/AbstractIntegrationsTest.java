package br.com.erudio.integrationstests.testcontainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationsTest.Initializer.class)
public class AbstractIntegrationsTest {


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17.2");

        private static void startContainers(){
            Startables.deepStart(Stream.of(postgreSQLContainer)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username", postgreSQLContainer.getUsername(),
                    "spring.datasource.password", postgreSQLContainer.getPassword()
            );
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource propertySource = new MapPropertySource(
            "testcontainers",
                    (Map) createConnectionConfiguration());
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}
