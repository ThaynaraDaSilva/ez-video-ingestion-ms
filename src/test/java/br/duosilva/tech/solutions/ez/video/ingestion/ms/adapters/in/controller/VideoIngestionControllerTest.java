package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.in.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusResponseDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoIngestionUseCase;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoStatusUseCase;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

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
		MultipartFile[] files = { new MockMultipartFile("files", "video.mp4", "video/mp4", "test content".getBytes()) };

		// Act
		ResponseEntity<Void> response = controller.uploadVideo(files);

		// Assert
		assertEquals(202, response.getStatusCodeValue());
		verify(videoIngestionUseCase, times(1)).ingestVideo(files, "6c0dc669-a18e-40d1-93ea-ba328a8daaed",
				"thaynara-r@hotmail.com");
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

	@Test
	void getVideoStatus_shouldReturnListOfVideos() {
		// Arrange
		String userEmail = "thaynara-r@hotmail.com";

		List<VideoStatusResponseDto> mockResponse = List.of(
				new VideoStatusResponseDto("video-1", "tom-jerry.mp4", LocalDateTime.now(), "https://s3/result.zip",
						ProcessingStatus.COMPLETED, userEmail),
				new VideoStatusResponseDto("video-2", "looney-tunes.mp4", LocalDateTime.now(), "https://s3/result2.zip",
						ProcessingStatus.FAILED, userEmail));

		when(videoStatusUseCase.listVideosByUserEmail(userEmail)).thenReturn(mockResponse);

		// Act
		ResponseEntity<?> response = controller.getVideoStatus(userEmail);

		// Assert
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(mockResponse, response.getBody());
		verify(videoStatusUseCase, times(1)).listVideosByUserEmail(userEmail);
	}
}
