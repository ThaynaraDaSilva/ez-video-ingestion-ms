package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Import;

@Import(HttpClientProperties.class)
public class HttpClientPropertiesTest {
	

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues("microservice.notification-endpoint=https://notification.example.com");

        @Test
        void shouldLoadNotificationEndpointFromProperties() {
            contextRunner.run(context -> {
                HttpClientProperties props = context.getBean(HttpClientProperties.class);
                assertThat(props.getNotificationEndpoint()).isEqualTo("https://notification.example.com");
            });
        }

        @EnableConfigurationProperties(HttpClientProperties.class)
        static class TestConfig {
            // Nada aqui, apenas ativa o binding da config
        }
}
