package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class VideoMetadata {

	private final String videoId;
	private final String originalFileName;
	private final String contentType;
	private final long fileSizeBytes;

	private final Long videoDuration;

	private final LocalDateTime uploadedAt;

	private final String userId;
	private final String userEmail;

	private ProcessingStatus status;
	private String errorMessage;

	private String resultBucketName;
	private String resultObjectKey;
	private LocalDateTime processedAt;
	private boolean notificationSent = false;


	public VideoMetadata(String videoId, String originalFileName, String contentType, long fileSizeBytes, Long videoDuration,
			String userId, String userEmail, ProcessingStatus status, String errorMessage, String resultBucketName,
			String resultObjectKey, LocalDateTime processedAt) {
		super();
		this.videoId = videoId;
		this.originalFileName = originalFileName;
		this.contentType = contentType;
		this.fileSizeBytes = fileSizeBytes;
		this.videoDuration = videoDuration;
		this.uploadedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		this.userId = userId;
		this.userEmail = userEmail;
		this.status = status;
		this.errorMessage = errorMessage;
		this.resultBucketName = resultBucketName;
		this.resultObjectKey = resultObjectKey;
		this.processedAt = processedAt;
	}

	public void markAsProcessing() {
		this.status = ProcessingStatus.PENDING;
	}

	public void markAsCompleted(String resultBucketName, String resultObjectKey) {
		this.status = ProcessingStatus.COMPLETED;
		this.resultBucketName = resultBucketName;
		this.resultObjectKey = resultObjectKey;
		this.processedAt = LocalDateTime.now();
	}

	public void markAsFailed(String errorMessage) {
		this.status = ProcessingStatus.FAILED;
		this.errorMessage = errorMessage;
		this.processedAt = LocalDateTime.now();
		this.notificationSent = true;

	}

	public ProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessingStatus status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResultBucketName() {
		return resultBucketName;
	}

	public void setResultBucketName(String resultBucketName) {
		this.resultBucketName = resultBucketName;
	}

	public String getResultObjectKey() {
		return resultObjectKey;
	}

	public void setResultObjectKey(String resultObjectKey) {
		this.resultObjectKey = resultObjectKey;
	}

	public LocalDateTime getProcessedAt() {
		return processedAt;
	}

	public void setProcessedAt(LocalDateTime processedAt) {
		this.processedAt = processedAt;
	}

	public String getVideoId() {
		return videoId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public String getContentType() {
		return contentType;
	}

	public long getFileSizeBytes() {
		return fileSizeBytes;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserEmail() {
		return userEmail;
	}
	
	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

}
