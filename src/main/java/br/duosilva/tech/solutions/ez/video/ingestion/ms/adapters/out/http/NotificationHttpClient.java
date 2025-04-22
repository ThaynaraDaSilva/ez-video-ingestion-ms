package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.NotificationRequest;

@FeignClient(name = "notificationClient", url = "${microservice.notification-endpoint}")
public interface NotificationHttpClient {
	
	@PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	String sendNotification(@RequestBody NotificationRequest request);

}
