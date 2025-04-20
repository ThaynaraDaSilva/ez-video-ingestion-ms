package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import java.time.LocalDateTime;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

public class VideoStatusResponseDto {

	private String videoId;
	private String originalFileName;
	private LocalDateTime processedAt;
	private String resultObjectKey;
	private ProcessingStatus status;
	private String userEmail;

	public VideoStatusResponseDto(String videoId, String originalFileName, LocalDateTime processedAt,
			String resultObjectKey, ProcessingStatus status, String userEmail) {
		this.videoId = videoId;
		this.originalFileName = originalFileName;
		this.processedAt = processedAt;
		this.resultObjectKey = resultObjectKey;
		this.status = status;
		this.userEmail = userEmail;
	}

	public String getVideoId() {
		return videoId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public LocalDateTime getProcessedAt() {
		return processedAt;
	}

	public String getResultObjectKey() {
		return resultObjectKey;
	}

	public ProcessingStatus getStatus() {
		return status;
	}

	public String getUserEmail() {
		return userEmail;
	}
}
