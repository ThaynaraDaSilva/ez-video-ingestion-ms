package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.policy.VideoUploadPolicy;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.ErrorMessages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoUploadPolicyServiceTest {

    @Mock
    private VideoUploadPolicy uploadPolicy;

    @Mock
    private MultipartFile file1;

    @Mock
    private MultipartFile file2;

    private VideoUploadPolicyService service;

    @BeforeEach
    void setUp() {
        service = new VideoUploadPolicyService(uploadPolicy);
    }

    @Test
    void validateFileSize_ValidSize_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxFileSizeBytes()).thenReturn(1000L);
        when(file1.getSize()).thenReturn(500L);

        // Act & Assert
        assertDoesNotThrow(() -> service.validateFileSize(file1));
    }

    @Test
    void validateFileSize_SizeExceedsLimit_ThrowsBusinessRuleException() {
        // Arrange
        when(uploadPolicy.getMaxFileSizeBytes()).thenReturn(1000L);
        when(file1.getSize()).thenReturn(1500L);

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.validateFileSize(file1));
        assertEquals(ErrorMessages.FILE_SIZE_EXCEEDED_FREE_PLAN, exception.getMessage());
    }

    @Test
    void validateUserDailyUploadLimit_UnderLimit_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxUploadsPerDay()).thenReturn(3);

        // Act & Assert
        assertDoesNotThrow(() -> service.validateUserDailyUploadLimit("user123"));
    }

    @Test
    void validateUserDailyUploadLimit_AtOrOverLimit_ThrowsBusinessRuleException() {
        // Arrange
        when(uploadPolicy.getMaxUploadsPerDay()).thenReturn(2);

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.validateUserDailyUploadLimit("user123"));
        assertEquals(ErrorMessages.UPLOAD_LIMIT_EXCEEDED_FREE_PLAN, exception.getMessage());
    }

    @Test
    void validateMaxFilesPerRequest_ValidNumberOfFiles_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxFilesPerRequest()).thenReturn(2);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(false);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        assertDoesNotThrow(() -> service.validateMaxFilesPerRequest(files));
    }

    @Test
    void validateMaxFilesPerRequest_ExceedsMaxFiles_ThrowsBusinessRuleException() {
        // Arrange
        when(uploadPolicy.getMaxFilesPerRequest()).thenReturn(1);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(false);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.validateMaxFilesPerRequest(files));
        assertEquals(ErrorMessages.MAX_FILES_PER_REQUEST_EXCEEDED, exception.getMessage());
    }

    @Test
    void validateMaxFilesPerRequest_EmptyFilesIgnored_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxFilesPerRequest()).thenReturn(1);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(true);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        assertDoesNotThrow(() -> service.validateMaxFilesPerRequest(files));
    }

    @Test
    void validateTotalSizePerRequest_ValidTotalSize_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxTotalRequestSizeBytes()).thenReturn(2000L);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(false);
        when(file1.getSize()).thenReturn(500L);
        when(file2.getSize()).thenReturn(1000L);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        assertDoesNotThrow(() -> service.validateTotalSizePerRequest(files));
    }

    @Test
    void validateTotalSizePerRequest_ExceedsTotalSize_ThrowsBusinessRuleException() {
        // Arrange
        when(uploadPolicy.getMaxTotalRequestSizeBytes()).thenReturn(1000L);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(false);
        when(file1.getSize()).thenReturn(600L);
        when(file2.getSize()).thenReturn(600L);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> service.validateTotalSizePerRequest(files));
        assertEquals(ErrorMessages.TOTAL_SIZE_EXCEEDED_FREE_PLAN, exception.getMessage());
    }

    @Test
    void validateTotalSizePerRequest_EmptyFilesIgnored_DoesNotThrow() {
        // Arrange
        when(uploadPolicy.getMaxTotalRequestSizeBytes()).thenReturn(1000L);
        when(file1.isEmpty()).thenReturn(false);
        when(file2.isEmpty()).thenReturn(true);
        when(file1.getSize()).thenReturn(500L);
        MultipartFile[] files = {file1, file2};

        // Act & Assert
        assertDoesNotThrow(() -> service.validateTotalSizePerRequest(files));
    }
}