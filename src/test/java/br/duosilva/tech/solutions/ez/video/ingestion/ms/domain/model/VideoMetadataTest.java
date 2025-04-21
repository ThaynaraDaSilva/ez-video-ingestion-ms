package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class VideoMetadataTest {
	   @Test
	    void testConstructorWithMinimalFields() {
	        String videoId = "vid123";
	        String originalFileName = "video.mp4";
	        String userEmail = "user@example.com";
	        ProcessingStatus status = ProcessingStatus.PENDING;
	        String resultObjectKey = "result/video.zip";
	        LocalDateTime processedAt = LocalDateTime.now();

	        VideoMetadata metadata = new VideoMetadata(videoId, originalFileName, userEmail, status, resultObjectKey, processedAt);

	        assertEquals(videoId, metadata.getVideoId());
	        assertEquals(originalFileName, metadata.getOriginalFileName());
	        assertEquals(userEmail, metadata.getUserEmail());
	        assertEquals(status, metadata.getStatus());
	        assertEquals(resultObjectKey, metadata.getResultObjectKey());
	        assertEquals(processedAt, metadata.getProcessedAt());
	    }

	    @Test
	    void testConstructorWithAllFieldsSetsUploadedAt() {
	        String videoId = "vid456";
	        String originalFileName = "video2.mp4";
	        String contentType = "video/mp4";
	        long fileSize = 1024L;
	        Long duration = 120L;
	        String userId = "user123";
	        String userEmail = "user2@example.com";
	        ProcessingStatus status = ProcessingStatus.PENDING;
	        String errorMessage = "Some error";
	        String resultBucket = "bucket-name";
	        String resultKey = "result/file.zip";
	        LocalDateTime processedAt = LocalDateTime.now();

	        VideoMetadata metadata = new VideoMetadata(videoId, originalFileName, contentType, fileSize, duration,
	                userId, userEmail, status, errorMessage, resultBucket, resultKey, processedAt);

	        assertEquals(videoId, metadata.getVideoId());
	        assertEquals(contentType, metadata.getContentType());
	        assertEquals(fileSize, metadata.getFileSizeBytes());
	        assertEquals(duration, metadata.getVideoDuration());
	        assertEquals(userId, metadata.getUserId());
	        assertEquals(userEmail, metadata.getUserEmail());
	        assertEquals(status, metadata.getStatus());
	        assertEquals(errorMessage, metadata.getErrorMessage());
	        assertEquals(resultBucket, metadata.getResultBucketName());
	        assertEquals(resultKey, metadata.getResultObjectKey());
	        assertEquals(processedAt, metadata.getProcessedAt());
	        assertNotNull(metadata.getUploadedAt());
	    }

	    @Test
	    void testMarkAsProcessing() {
	        VideoMetadata metadata = new VideoMetadata("vid", "file", "user@example.com",
	                ProcessingStatus.COMPLETED, "key", LocalDateTime.now());

	        metadata.markAsProcessing();
	        assertEquals(ProcessingStatus.PENDING, metadata.getStatus());
	    }

	    @Test
	    void testMarkAsCompleted() {
	        VideoMetadata metadata = new VideoMetadata("vid", "file", "user@example.com",
	                ProcessingStatus.PENDING, "key", LocalDateTime.now());

	        String bucket = "result-bucket";
	        String key = "result/key.zip";

	        metadata.markAsCompleted(bucket, key);

	        assertEquals(ProcessingStatus.COMPLETED, metadata.getStatus());
	        assertEquals(bucket, metadata.getResultBucketName());
	        assertEquals(key, metadata.getResultObjectKey());
	        assertNotNull(metadata.getProcessedAt());
	        assertTrue(ChronoUnit.SECONDS.between(metadata.getProcessedAt(), LocalDateTime.now()) < 5);
	    }

	    @Test
	    void testMarkAsFailed() {
	        VideoMetadata metadata = new VideoMetadata("vid", "file", "user@example.com",
	                ProcessingStatus.PENDING, "key", LocalDateTime.now());

	        String error = "Processing failed";

	        metadata.markAsFailed(error);

	        assertEquals(ProcessingStatus.FAILED, metadata.getStatus());
	        assertEquals(error, metadata.getErrorMessage());
	        assertTrue(metadata.isNotificationSent());
	        assertNotNull(metadata.getProcessedAt());
	    }

	    @Test
	    void testSettersAndGetters() {
	        VideoMetadata metadata = new VideoMetadata("vid", "file", "user@example.com",
	                ProcessingStatus.PENDING, "key", LocalDateTime.now());

	        metadata.setStatus(ProcessingStatus.COMPLETED);
	        metadata.setErrorMessage("error");
	        metadata.setResultBucketName("bucket");
	        metadata.setResultObjectKey("object-key");
	        LocalDateTime now = LocalDateTime.now();
	        metadata.setProcessedAt(now);
	        metadata.setNotificationSent(true);

	        assertEquals(ProcessingStatus.COMPLETED, metadata.getStatus());
	        assertEquals("error", metadata.getErrorMessage());
	        assertEquals("bucket", metadata.getResultBucketName());
	        assertEquals("object-key", metadata.getResultObjectKey());
	        assertEquals(now, metadata.getProcessedAt());
	        assertTrue(metadata.isNotificationSent());
	    }

}
