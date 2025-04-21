package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

public class VideoMetadataEntityTest {
	@Test
	void shouldSetAndGetAllFieldsCorrectly() {
		VideoMetadataEntity entity = new VideoMetadataEntity();

		String videoId = "vid123";
		String originalFileName = "video.mp4";
		String contentType = "video/mp4";
		Long fileSizeBytes = 1048576L;
		Long videoDuration = 120L;
		String userId = "user001";
		String userEmail = "user@example.com";
		ProcessingStatus status = ProcessingStatus.COMPLETED;
		String errorMessage = "No errors";
		String resultBucketName = "bucket-name";
		String resultObjectKey = "path/to/object";
		LocalDateTime processedAt = LocalDateTime.now();
		boolean notificationSent = true;

		entity.setVideoId(videoId);
		entity.setOriginalFileName(originalFileName);
		entity.setContentType(contentType);
		entity.setFileSizeBytes(fileSizeBytes);
		entity.setVideoDuration(videoDuration);
		entity.setUserId(userId);
		entity.setUserEmail(userEmail);
		entity.setStatus(status);
		entity.setErrorMessage(errorMessage);
		entity.setResultBucketName(resultBucketName);
		entity.setResultObjectKey(resultObjectKey);
		entity.setProcessedAt(processedAt);
		entity.setNotificationSent(notificationSent);

		assertEquals(videoId, entity.getVideoId());
		assertEquals(originalFileName, entity.getOriginalFileName());
		assertEquals(contentType, entity.getContentType());
		assertEquals(fileSizeBytes, entity.getFileSizeBytes());
		assertEquals(videoDuration, entity.getVideoDuration());
		assertEquals(userId, entity.getUserId());
		assertEquals(userEmail, entity.getUserEmail());
		assertEquals(status, entity.getStatus());
		assertEquals(errorMessage, entity.getErrorMessage());
		assertEquals(resultBucketName, entity.getResultBucketName());
		assertEquals(resultObjectKey, entity.getResultObjectKey());
		assertEquals(processedAt, entity.getProcessedAt());
		assertTrue(entity.isNotificationSent());
	}

	@Test
	void shouldCreateObjectWithAllArgsConstructor() {
		LocalDateTime now = LocalDateTime.now();

		VideoMetadataEntity entity = new VideoMetadataEntity("v001", "file.mp4", "video/mp4", 2048L, 60L, "user1",
				"user1@example.com", ProcessingStatus.PENDING, null, "my-bucket", "result.zip", now);

		assertEquals("v001", entity.getVideoId());
		assertEquals("file.mp4", entity.getOriginalFileName());
		assertEquals("video/mp4", entity.getContentType());
		assertEquals(2048L, entity.getFileSizeBytes());
		assertEquals(60L, entity.getVideoDuration());
		assertEquals("user1", entity.getUserId());
		assertEquals("user1@example.com", entity.getUserEmail());
		assertEquals(ProcessingStatus.PENDING, entity.getStatus());
		assertNull(entity.getErrorMessage());
		assertEquals("my-bucket", entity.getResultBucketName());
		assertEquals("result.zip", entity.getResultObjectKey());
		assertEquals(now, entity.getProcessedAt());
		assertFalse(entity.isNotificationSent()); // default is false
	}

}
