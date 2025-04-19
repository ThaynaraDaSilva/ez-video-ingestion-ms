package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VideoUploadPolicyTest {

    private VideoUploadPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new VideoUploadPolicy();
    }

    @Test
    void shouldReturnCorrectFrameExtractionInterval() {
        assertEquals(1000L, policy.getFrameExtractionIntervalInMillis());
    }

    @Test
    void shouldReturnCorrectMaxFileSize() {
        long expected = 50 * 1024 * 1024L;
        assertEquals(expected, policy.getMaxFileSizeBytes());
    }

    @Test
    void shouldReturnCorrectMaxTotalRequestSize() {
        long expected = 100 * 1024 * 1024L;
        assertEquals(expected, policy.getMaxTotalRequestSizeBytes());
    }

    @Test
    void shouldReturnCorrectMaxFilesPerRequest() {
        assertEquals(3, policy.getMaxFilesPerRequest());
    }

    @Test
    void shouldReturnCorrectMaxUploadsPerDay() {
        assertEquals(5, policy.getMaxUploadsPerDay());
    }

    @Test
    void shouldReturnCorrectMaxDailyTotalSize() {
        long expected = 300 * 1024 * 1024L;
        assertEquals(expected, policy.getMaxDailyTotalSizeBytes());
    }
}
