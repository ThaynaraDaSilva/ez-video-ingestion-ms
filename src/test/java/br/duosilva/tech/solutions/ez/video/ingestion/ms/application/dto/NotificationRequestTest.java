package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class NotificationRequestTest {

	@Test
	void shouldCreateEmptyNotificationRequest() {
		NotificationRequest request = new NotificationRequest();

		assertNull(request.getVideoId());
		assertNull(request.getStatus());
		assertNull(request.getErrorMessage());
		assertNull(request.getEmail());
	}

	@Test
	void shouldSetAndGetFieldsCorrectly() {
		NotificationRequest request = new NotificationRequest();

		request.setVideoId("vid123");
		request.setStatus("failed");
		request.setErrorMessage("Erro de processamento");
		request.setEmail("user@example.com");

		assertEquals("vid123", request.getVideoId());
		assertEquals("failed", request.getStatus());
		assertEquals("Erro de processamento", request.getErrorMessage());
		assertEquals("user@example.com", request.getEmail());
	}

	@Test
	void shouldCreateNotificationRequestUsingConstructor() {
		NotificationRequest request = new NotificationRequest("vid456", "completed", null, "anotheruser@example.com");

		assertEquals("vid456", request.getVideoId());
		assertEquals("completed", request.getStatus());
		assertNull(request.getErrorMessage());
		assertEquals("anotheruser@example.com", request.getEmail());
	}
}
