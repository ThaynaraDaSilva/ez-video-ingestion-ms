package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence.VideoMetadataEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class VideoMetadataMapperTest {

    private static final String VIDEO_ID = "vid123";
    private static final String ORIGINAL_FILE_NAME = "test_video.mp4";
    private static final String CONTENT_TYPE = "video/mp4";
    private static final Long FILE_SIZE_BYTES = 1024L;
    private static final Long VIDEO_DURATION = 120L;
    private static final String USER_ID = "user123";
    private static final String USER_EMAIL = "test@example.com";
    private static final ProcessingStatus STATUS = ProcessingStatus.COMPLETED;
    private static final String ERROR_MESSAGE = "Processing failed";
    private static final String RESULT_BUCKET_NAME = "video-bucket";
    private static final String RESULT_OBJECT_KEY = "processed/videos/output.mp4";
    private static final LocalDateTime PROCESSED_AT = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));

    @Test
    void testToEntity_FullyPopulated() {
        // Arrange
        VideoMetadata domain = new VideoMetadata(
                VIDEO_ID,
                ORIGINAL_FILE_NAME,
                CONTENT_TYPE,
                FILE_SIZE_BYTES,
                VIDEO_DURATION,
                USER_ID,
                USER_EMAIL,
                STATUS,
                ERROR_MESSAGE,
                RESULT_BUCKET_NAME,
                RESULT_OBJECT_KEY,
                PROCESSED_AT
        );

        // Act
        VideoMetadataEntity entity = VideoMetadataMapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(VIDEO_ID, entity.getVideoId());
        assertEquals(ORIGINAL_FILE_NAME, entity.getOriginalFileName());
        assertEquals(CONTENT_TYPE, entity.getContentType());
        assertEquals(FILE_SIZE_BYTES, entity.getFileSizeBytes());
        assertEquals(VIDEO_DURATION, entity.getVideoDuration());
        assertEquals(USER_ID, entity.getUserId());
        assertEquals(USER_EMAIL, entity.getUserEmail());
        assertEquals(STATUS, entity.getStatus());
        assertEquals(ERROR_MESSAGE, entity.getErrorMessage());
        assertEquals(RESULT_BUCKET_NAME, entity.getResultBucketName());
        assertEquals(RESULT_OBJECT_KEY, entity.getResultObjectKey());
        assertEquals(PROCESSED_AT, entity.getProcessedAt());
    }

    @Test
    void testToEntity_NullFields() {
        // Arrange
        VideoMetadata domain = new VideoMetadata(
                VIDEO_ID,
                ORIGINAL_FILE_NAME,
                CONTENT_TYPE,
                FILE_SIZE_BYTES,
                null, // videoDuration
                USER_ID,
                USER_EMAIL,
                STATUS,
                null, // errorMessage
                null, // resultBucketName
                null, // resultObjectKey
                null  // processedAt
        );

        // Act
        VideoMetadataEntity entity = VideoMetadataMapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(VIDEO_ID, entity.getVideoId());
        assertEquals(ORIGINAL_FILE_NAME, entity.getOriginalFileName());
        assertEquals(CONTENT_TYPE, entity.getContentType());
        assertEquals(FILE_SIZE_BYTES, entity.getFileSizeBytes());
        assertNull(entity.getVideoDuration());
        assertEquals(USER_ID, entity.getUserId());
        assertEquals(USER_EMAIL, entity.getUserEmail());
        assertEquals(STATUS, entity.getStatus());
        assertNull(entity.getErrorMessage());
        assertNull(entity.getResultBucketName());
        assertNull(entity.getResultObjectKey());
        assertNull(entity.getProcessedAt());
    }

    @Test
    void testToDomain_FullyPopulated() {
        // Arrange
        VideoMetadataEntity entity = new VideoMetadataEntity();
        entity.setVideoId(VIDEO_ID);
        entity.setOriginalFileName(ORIGINAL_FILE_NAME);
        entity.setContentType(CONTENT_TYPE);
        entity.setFileSizeBytes(FILE_SIZE_BYTES);
        entity.setVideoDuration(VIDEO_DURATION);
        entity.setUserId(USER_ID);
        entity.setUserEmail(USER_EMAIL);
        entity.setStatus(STATUS);
        entity.setErrorMessage(ERROR_MESSAGE);
        entity.setResultBucketName(RESULT_BUCKET_NAME);
        entity.setResultObjectKey(RESULT_OBJECT_KEY);
        entity.setProcessedAt(PROCESSED_AT);

        // Act
        VideoMetadata domain = VideoMetadataMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(VIDEO_ID, domain.getVideoId());
        assertEquals(ORIGINAL_FILE_NAME, domain.getOriginalFileName());
        assertEquals(CONTENT_TYPE, domain.getContentType());
        assertEquals(FILE_SIZE_BYTES, domain.getFileSizeBytes());
        assertEquals(VIDEO_DURATION, domain.getVideoDuration());
        assertEquals(USER_ID, domain.getUserId());
        assertEquals(USER_EMAIL, domain.getUserEmail());
        assertEquals(STATUS, domain.getStatus());
        assertEquals(ERROR_MESSAGE, domain.getErrorMessage());
        assertEquals(RESULT_BUCKET_NAME, domain.getResultBucketName());
        assertEquals(RESULT_OBJECT_KEY, domain.getResultObjectKey());
        assertEquals(PROCESSED_AT, domain.getProcessedAt());
    }

    @Test
    void testToDomain_NullFields() {
        // Arrange
        VideoMetadataEntity entity = new VideoMetadataEntity();
        entity.setVideoId(VIDEO_ID);
        entity.setOriginalFileName(ORIGINAL_FILE_NAME);
        entity.setContentType(CONTENT_TYPE);
        entity.setFileSizeBytes(FILE_SIZE_BYTES);
        entity.setVideoDuration(null);
        entity.setUserId(USER_ID);
        entity.setUserEmail(USER_EMAIL);
        entity.setStatus(STATUS);
        entity.setErrorMessage(null);
        entity.setResultBucketName(null);
        entity.setResultObjectKey(null);
        entity.setProcessedAt(null);

        // Act
        VideoMetadata domain = VideoMetadataMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(VIDEO_ID, domain.getVideoId());
        assertEquals(ORIGINAL_FILE_NAME, domain.getOriginalFileName());
        assertEquals(CONTENT_TYPE, domain.getContentType());
        assertEquals(FILE_SIZE_BYTES, domain.getFileSizeBytes());
        assertNull(domain.getVideoDuration());
        assertEquals(USER_ID, domain.getUserId());
        assertEquals(USER_EMAIL, domain.getUserEmail());
        assertEquals(STATUS, domain.getStatus());
        assertNull(domain.getErrorMessage());
        assertNull(domain.getResultBucketName());
        assertNull(domain.getResultObjectKey());
        assertNull(domain.getProcessedAt());
    }
}