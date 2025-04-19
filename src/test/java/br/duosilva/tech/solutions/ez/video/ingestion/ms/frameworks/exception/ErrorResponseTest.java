package br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    @Test
    void testConstructor_WithValidMessage() {
        // Arrange
        String message = "An error occurred";

        // Act
        ErrorResponse errorResponse = new ErrorResponse(message);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void testConstructor_WithNullMessage() {
        // Act
        ErrorResponse errorResponse = new ErrorResponse(null);

        // Assert
        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
    }

    @Test
    void testSetMessage_WithValidMessage() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse("Initial message");
        String newMessage = "Updated error message";

        // Act
        errorResponse.setMessage(newMessage);

        // Assert
        assertEquals(newMessage, errorResponse.getMessage());
    }

    @Test
    void testSetMessage_WithNullMessage() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse("Initial message");

        // Act
        errorResponse.setMessage(null);

        // Assert
        assertNull(errorResponse.getMessage());
    }

    @Test
    void testSetMessage_WithEmptyMessage() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse("Initial message");
        String emptyMessage = "";

        // Act
        errorResponse.setMessage(emptyMessage);

        // Assert
        assertEquals(emptyMessage, errorResponse.getMessage());
    }

    @Test
    void testGetMessage_AfterConstructor() {
        // Arrange
        String message = "Test error";
        ErrorResponse errorResponse = new ErrorResponse(message);

        // Act
        String retrievedMessage = errorResponse.getMessage();

        // Assert
        assertEquals(message, retrievedMessage);
    }
}