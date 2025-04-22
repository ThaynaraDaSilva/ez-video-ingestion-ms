package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

public class NotificationRequest    {
    private String videoId;
    private String status;
    private String errorMessage;
    private String email;

    public NotificationRequest() {
    }

    public NotificationRequest(String videoId, String status, String errorMessage, String email) {
        this.videoId = videoId;
        this.status = status;
        this.errorMessage = errorMessage;
        this.email = email;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}