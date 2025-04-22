package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.messaging.AmazonSQSAdapter;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.s3.AmazonS3Adapter;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoIngestionMessage;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusResponseDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.service.VideoUploadPolicyService;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.ErrorMessages;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.s3.S3KeyGenerator;

@ExtendWith(MockitoExtension.class)
class VideoIngestionUseCaseTest {

    @Mock
    private VideoUploadPolicyService videoUploadPolicyService;

    @Mock
    private AmazonS3Adapter amazonS3Adapter;

    @Mock
    private AmazonSQSAdapter amazonSQSAdapter;

    @Mock
    private VideoMetadataRepository videoMetadataRepository;

    @InjectMocks
    private VideoIngestionUseCase videoIngestionUseCase;
    
    @InjectMocks
    private VideoStatusUseCase videoStatusUseCase;

    private MultipartFile mockFile;
    private String userId = "user123";
    private String userEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        mockFile = mock(MultipartFile.class);
    }

    @Test
    void listVideosByUserEmail_shouldReturnList_whenVideosExist() {
        // Arrange
        String userEmail = "test@example.com";
        LocalDateTime processedAt = LocalDateTime.of(2025, 4, 20, 14, 0);

        List<VideoMetadata> videoList = List.of(
                new VideoMetadata(
                        "abc123",              // videoId
                        "video.mp4",           // originalFileName
                        userEmail,             // userEmail
                        ProcessingStatus.COMPLETED, // status
                        "processed/output.zip",     // resultObjectKey
                        processedAt                 // processedAt
                )
        );

        when(videoMetadataRepository.findByUserEmail(userEmail)).thenReturn(videoList);

        // Act
        List<VideoStatusResponseDto> result = videoStatusUseCase.listVideosByUserEmail(userEmail);

        // Assert
        assertEquals(1, result.size());

        VideoStatusResponseDto dto = result.get(0);
        assertEquals("abc123", dto.getVideoId());
        assertEquals("video.mp4", dto.getOriginalFileName());
        assertEquals(processedAt, dto.getProcessedAt());
        assertEquals("processed/output.zip", dto.getResultObjectKey());
        assertEquals(ProcessingStatus.COMPLETED, dto.getStatus());
        assertEquals(userEmail, dto.getUserEmail());

        verify(videoMetadataRepository, times(1)).findByUserEmail(userEmail);
    }

    @Test
    void listVideosByUserEmail_shouldThrowException_whenNoVideosFound() {
        // Arrange
        String userEmail = "empty@example.com";
        when(videoMetadataRepository.findByUserEmail(userEmail)).thenReturn(List.of());

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () ->
        videoStatusUseCase.listVideosByUserEmail(userEmail));

        assertEquals("No videos processed for the requested email.", exception.getMessage());
        verify(videoMetadataRepository, times(1)).findByUserEmail(userEmail);
    }
    
    @Test
    void testIngestVideo_SuccessfulUpload() {
        MultipartFile[] files = { mockFile };
        String s3Key = "users/user123/videos/test_video.mp4";

        when(mockFile.getOriginalFilename()).thenReturn("test_video.mp4");
        when(mockFile.getContentType()).thenReturn("video/mp4");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        when(amazonS3Adapter.getBucketName()).thenReturn("test-bucket");
        when(amazonS3Adapter.doesFileExistInS3(anyString())).thenReturn(false);

        try (var mockedStatic = mockStatic(S3KeyGenerator.class)) {
            mockedStatic.when(() -> S3KeyGenerator.generateS3Key(eq(userId), eq(mockFile), anyString())).thenReturn(s3Key);

            videoIngestionUseCase.ingestVideo(files, userId, userEmail);
            
            verify(videoUploadPolicyService).validateMaxFilesPerRequest(any(MultipartFile[].class));
            verify(amazonS3Adapter).uploadFileToS3(s3Key, mockFile);
            verify(videoMetadataRepository).save(any(VideoMetadata.class));
            verify(amazonSQSAdapter).publishVideoIngestionMessage(any(VideoIngestionMessage.class));
        }
    }

    @Test
    void testIngestVideo_NoFilesProvided_ThrowsBusinessRuleException() {
        MultipartFile[] files = null;

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> videoIngestionUseCase.ingestVideo(files, userId, userEmail));
        assertEquals(ErrorMessages.NO_VIDEO_PROVIDED, exception.getMessage());
        verifyNoInteractions(videoUploadPolicyService, amazonS3Adapter, amazonSQSAdapter, videoMetadataRepository);
        
    }

    @Test
    void testIngestVideo_EmptyFile_SkipsProcessing() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);
        when(emptyFile.isEmpty()).thenReturn(true);
        MultipartFile[] files = { emptyFile };

        videoIngestionUseCase.ingestVideo(files, userId, userEmail);

        verify(videoUploadPolicyService).validateMaxFilesPerRequest(any());
        verifyNoInteractions(amazonS3Adapter, amazonSQSAdapter, videoMetadataRepository);
    }

    @Test
    void testIngestVideo_FileAlreadyExistsInS3_SkipsUpload() {
        MultipartFile[] files = { mockFile };
        String s3Key = "users/user123/videos/test_video.mp4";

        when(mockFile.getOriginalFilename()).thenReturn("test_video.mp4");
        when(mockFile.getContentType()).thenReturn("video/mp4");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        when(amazonS3Adapter.getBucketName()).thenReturn("test-bucket");
        when(amazonS3Adapter.doesFileExistInS3(anyString())).thenReturn(true);

        try (var mockedStatic = mockStatic(S3KeyGenerator.class)) {
            mockedStatic.when(() -> S3KeyGenerator.generateS3Key(eq(userId), eq(mockFile), anyString())).thenReturn(s3Key);

            videoIngestionUseCase.ingestVideo(files, userId, userEmail);
            verify(videoUploadPolicyService).validateMaxFilesPerRequest(files);
            verify(videoUploadPolicyService).validateFileSize(mockFile);
            verify(amazonS3Adapter, never()).uploadFileToS3(anyString(), any(MultipartFile.class));
            verify(videoMetadataRepository).save(any(VideoMetadata.class));
            verify(amazonSQSAdapter).publishVideoIngestionMessage(any(VideoIngestionMessage.class));
        }
    }


    @Test
    void testIngestVideo_MetadataSavedCorrectly() {
        MultipartFile[] files = { mockFile };
        String s3Key = "users/user123/videos/test_video.mp4";

        when(mockFile.getOriginalFilename()).thenReturn("test_video.mp4");
        when(mockFile.getContentType()).thenReturn("video/mp4");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        when(amazonS3Adapter.getBucketName()).thenReturn("test-bucket");
        when(amazonS3Adapter.doesFileExistInS3(anyString())).thenReturn(false);

        try (var mockedStatic = mockStatic(S3KeyGenerator.class)) {
            mockedStatic.when(() -> S3KeyGenerator.generateS3Key(eq(userId), eq(mockFile), anyString())).thenReturn(s3Key);

            videoIngestionUseCase.ingestVideo(files, userId, userEmail);

            verify(videoMetadataRepository).save(argThat(metadata ->
                    metadata.getOriginalFileName().equals("test_video.mp4") &&
                            metadata.getContentType().equals("video/mp4") &&
                            metadata.getFileSizeBytes() == 1024L &&
                            metadata.getUserId().equals(userId) &&
                            metadata.getUserEmail().equals(userEmail) &&
                            metadata.getStatus() == ProcessingStatus.PENDING
            ));
        }
    }

    @Test
    void testIngestVideo_SQSMessagePublishedCorrectly() {
        MultipartFile[] files = { mockFile };
        String s3Key = "users/user123/videos/test_video.mp4";

        when(mockFile.getOriginalFilename()).thenReturn("test_video.mp4");
        when(mockFile.getContentType()).thenReturn("video/mp4");
        when(mockFile.getSize()).thenReturn(1024L);
        when(mockFile.isEmpty()).thenReturn(false);
        when(amazonS3Adapter.getBucketName()).thenReturn("test-bucket");
        when(amazonS3Adapter.doesFileExistInS3(anyString())).thenReturn(false);

        try (var mockedStatic = mockStatic(S3KeyGenerator.class)) {
            mockedStatic.when(() -> S3KeyGenerator.generateS3Key(eq(userId), eq(mockFile), anyString())).thenReturn(s3Key);

            videoIngestionUseCase.ingestVideo(files, userId, userEmail);

            verify(amazonSQSAdapter).publishVideoIngestionMessage(argThat(message ->
                    message.getOriginalFileName().equals("test_video.mp4") &&
                            message.getS3BucketName().equals("test-bucket") &&
                            message.getS3Key().equals(s3Key) &&
                            message.getUserId().equals(userId) &&
                            message.getUserEmail().equals(userEmail)
            ));
        }
    }
}