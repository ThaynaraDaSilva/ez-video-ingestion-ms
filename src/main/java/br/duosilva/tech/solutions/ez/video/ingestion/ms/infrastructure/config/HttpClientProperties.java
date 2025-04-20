package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "microservice")
public class HttpClientProperties {
	
	private String notificationEndpoint;

    public String getNotificationEndpoint() {
        return notificationEndpoint;
    }

    public void setNotificationEndpoint(String notificationEndpoint) {
        this.notificationEndpoint = notificationEndpoint;
    }

}
