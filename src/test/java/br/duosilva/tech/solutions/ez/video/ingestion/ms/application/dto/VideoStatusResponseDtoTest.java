package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

public class VideoStatusResponseDtoTest {
	
	 private VideoStatusResponseDto dto;
	    private final String videoId = "abc123";
	    private final String originalFileName = "video.mp4";
	    private final LocalDateTime processedAt = LocalDateTime.of(2025, 4, 20, 14, 0);
	    private final String resultObjectKey = "processed/output.zip";
	    private final ProcessingStatus status = ProcessingStatus.COMPLETED;
	    private final String userEmail = "example@email.com";

	    @BeforeEach
	    void setUp() {
	        dto = new VideoStatusResponseDto(videoId, originalFileName, processedAt, resultObjectKey, status, userEmail);
	    }

	    @Test
	    void testGetters() {
	        assertEquals(videoId, dto.getVideoId());
	        assertEquals(originalFileName, dto.getOriginalFileName());
	        assertEquals(processedAt, dto.getProcessedAt());
	        assertEquals(resultObjectKey, dto.getResultObjectKey());
	        assertEquals(status, dto.getStatus());
	        assertEquals(userEmail, dto.getUserEmail());
	    }

	    @Test
	    void testJsonSerialization_shouldUseCorrectDateFormat() throws Exception {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.registerModule(new JavaTimeModule());
	        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	        String json = mapper.writeValueAsString(dto);
	        assertTrue(json.contains("\"processedAt\":\"2025-04-20 14:00:00\""));
	    }

}
