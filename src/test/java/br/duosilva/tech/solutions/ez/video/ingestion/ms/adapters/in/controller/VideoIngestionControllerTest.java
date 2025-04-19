package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.in.controller;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoIngestionUseCase;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoStatusUseCase;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VideoIngestionControllerUnitTest {

    private VideoIngestionUseCase videoIngestionUseCase;
    private VideoStatusUseCase videoStatusUseCase;
    private VideoIngestionController controller;

    @BeforeEach
    void setUp() {
        videoIngestionUseCase = mock(VideoIngestionUseCase.class);
        videoStatusUseCase = mock(VideoStatusUseCase.class);
        controller = new VideoIngestionController(videoIngestionUseCase, videoStatusUseCase);
    }

    @Test
    void uploadVideo_shouldReturnAccepted() {
        // Arrange
        MultipartFile[] files = {
                new MockMultipartFile("files", "video.mp4", "video/mp4", "test content".getBytes())
        };

        // Act
        ResponseEntity<Void> response = controller.uploadVideo(files);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        verify(videoIngestionUseCase, times(1))
                .ingestVideo(files, "6c0dc669-a18e-40d1-93ea-ba328a8daaed", "thaynara-r@hotmail.com");
    }

    @Test
    void updateVideoStatus_shouldReturnNoContent() {
        // Arrange
        String videoId = "video123";
        VideoStatusRequestDto requestDto = new VideoStatusRequestDto();
        requestDto.setStatus(ProcessingStatus.COMPLETED);
        requestDto.setResultObjectKey("processed/output.mp4");

        // Act
        ResponseEntity<Void> response = controller.updateVideoStatus(videoId, requestDto);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(videoStatusUseCase, times(1)).updateVideoStatus(videoId, requestDto);
    }
}
