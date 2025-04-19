package br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBusinessRuleException() {
        // Arrange
        BusinessRuleException exception = new BusinessRuleException("Business rule violated");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBusinessRuleException(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business rule violated", response.getBody().getMessage());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGenericException() {
        // Arrange
        Exception exception = new Exception("Something went wrong");

        // Act
        ResponseEntity<String> response = handler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error: Something went wrong", response.getBody());
    }
}
