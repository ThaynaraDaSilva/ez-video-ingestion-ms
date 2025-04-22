package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VideoIngestionMessageTest {

    private VideoIngestionMessage message;

    @BeforeEach
    void setUp() {
        message = new VideoIngestionMessage();
    }

    @Test
    void testSetAndGetVideoId() {
        String videoId = "vid123";
        message.setVideoId(videoId);
        assertEquals(videoId, message.getVideoId());
    }

    @Test
    void testSetAndGetOriginalFileName() {
        String fileName = "test_video.mp4";
        message.setOriginalFileName(fileName);
        assertEquals(fileName, message.getOriginalFileName());
    }

    @Test
    void testSetAndGetS3BucketName() {
        String bucketName = "test-bucket";
        message.setS3BucketName(bucketName);
        assertEquals(bucketName, message.getS3BucketName());
    }

    @Test
    void testSetAndGetS3Key() {
        String s3Key = "videos/test_video.mp4";
        message.setS3Key(s3Key);
        assertEquals(s3Key, message.getS3Key());
    }

    @Test
    void testSetAndGetUploadTimestamp() {
        String timestamp = "2023-01-01T12:00:00Z";
        message.setUploadTimestamp(timestamp);
        assertEquals(timestamp, message.getUploadTimestamp());
    }

    @Test
    void testSetAndGetUserId() {
        String userId = "user123";
        message.setUserId(userId);
        assertEquals(userId, message.getUserId());
    }

    @Test
    void testSetAndGetUserEmail() {
        String email = "test@example.com";
        message.setUserEmail(email);
        assertEquals(email, message.getUserEmail());
    }

    @Test
    void testToString() {
        message.setVideoId("vid123");
        message.setOriginalFileName("test_video.mp4");
        message.setS3BucketName("test-bucket");
        message.setS3Key("videos/test_video.mp4");
        message.setUploadTimestamp("2023-01-01T12:00:00Z");
        message.setUserId("user123");
        message.setUserEmail("test@example.com");

        String expected = "VideoIngestionMessage [videoId=vid123, originalFileName=test_video.mp4, s3BucketName=test-bucket, s3Key=videos/test_video.mp4, uploadTimestamp=2023-01-01T12:00:00Z, userId=user123, userEmail=test@example.com]";
        assertEquals(expected, message.toString());
    }

    @Test
    void testNullValues() {
        // Test that getters return null when no values are set
        assertNull(message.getVideoId());
        assertNull(message.getOriginalFileName());
        assertNull(message.getS3BucketName());
        assertNull(message.getS3Key());
        assertNull(message.getUploadTimestamp());
        assertNull(message.getUserId());
        assertNull(message.getUserEmail());
    }
}