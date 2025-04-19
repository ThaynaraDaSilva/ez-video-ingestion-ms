package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.s3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3KeyGeneratorTest {

    @Mock
    private MultipartFile mockFile;

    @Test
    void generateS3Key_ValidInput_ReturnsCorrectKey() {
        // Arrange
        String userId = "user123";
        String videoId = "video456";
        String filename = "testvideo.mp4";

        when(mockFile.getOriginalFilename()).thenReturn(filename);

        // Act
        String result = S3KeyGenerator.generateS3Key(userId, mockFile, videoId);

        // Assert
        assertEquals("user123/video456-testvideo.mp4", result);
    }

    @Test
    void generateS3Key_NullFilename_ThrowsBusinessRuleException() {
        // Arrange
        String userId = "user123";
        String videoId = "video456";

        when(mockFile.getOriginalFilename()).thenReturn(null);

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> S3KeyGenerator.generateS3Key(userId, mockFile, videoId));

        assertEquals("INVALID FILE NAME FOR S3 STORAGE: ORIGINAL FILE NAME IS NULL OR HAS NO EXTENSION",
                exception.getMessage());
    }

    @Test
    void generateS3Key_FilenameWithoutExtension_ThrowsBusinessRuleException() {
        // Arrange
        String userId = "user123";
        String videoId = "video456";

        when(mockFile.getOriginalFilename()).thenReturn("testvideo");

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> S3KeyGenerator.generateS3Key(userId, mockFile, videoId));

        assertEquals("INVALID FILE NAME FOR S3 STORAGE: ORIGINAL FILE NAME IS NULL OR HAS NO EXTENSION",
                exception.getMessage());
    }

    @Test
    void generateS3Key_FilenameWithMultipleDots_UsesLastExtension() {
        // Arrange
        String userId = "user123";
        String videoId = "video456";
        String filename = "test.video.file.mp4";

        when(mockFile.getOriginalFilename()).thenReturn(filename);

        // Act
        String result = S3KeyGenerator.generateS3Key(userId, mockFile, videoId);

        // Assert
        assertEquals("user123/video456-test.video.file.mp4", result);
    }

    @Test
    void generateS3Key_EmptyFilename_ThrowsBusinessRuleException() {
        // Arrange
        String userId = "user123";
        String videoId = "video456";

        when(mockFile.getOriginalFilename()).thenReturn("");

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> S3KeyGenerator.generateS3Key(userId, mockFile, videoId));

        assertEquals("INVALID FILE NAME FOR S3 STORAGE: ORIGINAL FILE NAME IS NULL OR HAS NO EXTENSION",
                exception.getMessage());
    }
}