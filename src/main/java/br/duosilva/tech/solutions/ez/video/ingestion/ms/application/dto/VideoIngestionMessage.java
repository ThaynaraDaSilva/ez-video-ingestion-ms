package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

public class VideoIngestionMessage {

	private String videoId;
	private String originalFileName;
	private String s3BucketName;
	private String s3Key;
	private String uploadTimestamp;
	private String userId;
	private String userEmail;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getS3BucketName() {
		return s3BucketName;
	}

	public void setS3BucketName(String s3BucketName) {
		this.s3BucketName = s3BucketName;
	}

	public String getS3Key() {
		return s3Key;
	}

	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}

	public String getUploadTimestamp() {
		return uploadTimestamp;
	}

	public void setUploadTimestamp(String uploadTimestamp) {
		this.uploadTimestamp = uploadTimestamp;
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

	@Override
	public String toString() {
		return "VideoIngestionMessage [videoId=" + videoId + ", originalFileName=" + originalFileName
				+ ", s3BucketName=" + s3BucketName + ", s3Key=" + s3Key + ", uploadTimestamp=" + uploadTimestamp
				+ ", userId=" + userId + ", userEmail=" + userEmail + "]";
	}



}
