package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoStatusRequestDtoTest {

    private VideoStatusRequestDto dto;

    @BeforeEach
    void setUp() {
        dto = new VideoStatusRequestDto();
    }

    @Test
    void testSetAndGetStatus() {
        ProcessingStatus status = ProcessingStatus.COMPLETED;
        dto.setStatus(status);
        assertEquals(status, dto.getStatus());
    }

    @Test
    void testSetAndGetResultObjectKey() {
        String resultObjectKey = "processed/videos/output.mp4";
        dto.setResultObjectKey(resultObjectKey);
        assertEquals(resultObjectKey, dto.getResultObjectKey());
    }

    @Test
    void testSetAndGetErrorMessage() {
        String errorMessage = "Processing failed due to invalid format";
        dto.setErrorMessage(errorMessage);
        assertEquals(errorMessage, dto.getErrorMessage());
    }

    @Test
    void testNullValues() {
        // Test that getters return null when no values are set
        assertNull(dto.getStatus());
        assertNull(dto.getResultObjectKey());
        assertNull(dto.getErrorMessage());
    }

    @Test
    void testDefaultConstructor() {
        VideoStatusRequestDto newDto = new VideoStatusRequestDto();
        assertNotNull(newDto);
        assertNull(newDto.getStatus());
        assertNull(newDto.getResultObjectKey());
        assertNull(newDto.getErrorMessage());
    }

    @Test
    void testMultipleStatusValues() {
        // Test different ProcessingStatus values
        dto.setStatus(ProcessingStatus.PENDING);
        assertEquals(ProcessingStatus.PENDING, dto.getStatus());

        dto.setStatus(ProcessingStatus.COMPLETED);
        assertEquals(ProcessingStatus.COMPLETED, dto.getStatus());

        dto.setStatus(ProcessingStatus.FAILED);
        assertEquals(ProcessingStatus.FAILED, dto.getStatus());
    }
}