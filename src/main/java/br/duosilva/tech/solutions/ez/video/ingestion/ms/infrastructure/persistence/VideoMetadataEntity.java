package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence;

import java.time.LocalDateTime;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class VideoMetadataEntity {

	private String videoId;
	private String originalFileName;
	private String contentType;
	private Long fileSizeBytes;
	private Long videoDuration;
	private String userId;
	private String userEmail;
	private ProcessingStatus status;
	private String errorMessage;
	private String resultBucketName;
	private String resultObjectKey;
	private LocalDateTime processedAt;
	private boolean notificationSent;
	

	public VideoMetadataEntity() {
		super();
	}

	public VideoMetadataEntity(String videoId, String originalFileName, String contentType, Long fileSizeBytes,
			Long videoDuration, String userId, String userEmail, ProcessingStatus status, String errorMessage,
			String resultBucketName, String resultObjectKey, LocalDateTime processedAt) {
		super();
		this.videoId = videoId;
		this.originalFileName = originalFileName;
		this.contentType = contentType;
		this.fileSizeBytes = fileSizeBytes;
		this.videoDuration = videoDuration;
		this.userId = userId;
		this.userEmail = userEmail;
		this.status = status;
		this.errorMessage = errorMessage;
		this.resultBucketName = resultBucketName;
		this.resultObjectKey = resultObjectKey;
		this.processedAt = processedAt;
	}

	@DynamoDbPartitionKey
	public String getVideoId() {
		return videoId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getFileSizeBytes() {
		return fileSizeBytes;
	}

	public void setFileSizeBytes(Long fileSizeBytes) {
		this.fileSizeBytes = fileSizeBytes;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public boolean isNotificationSent() {
		return notificationSent;
	}

	public void setNotificationSent(boolean notificationSent) {
		this.notificationSent = notificationSent;
	}

	
}
