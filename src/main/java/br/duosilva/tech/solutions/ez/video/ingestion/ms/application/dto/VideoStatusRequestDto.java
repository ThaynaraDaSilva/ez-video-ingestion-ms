package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import java.time.LocalDateTime;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoStatusRequestDto {
	
	@JsonProperty("status")
	private ProcessingStatus status;
	@JsonProperty("resultObjectKey")
    private String resultObjectKey;
	@JsonProperty("errorMessage")
    private String errorMessage;
	@JsonProperty("processedAt")
    private LocalDateTime processedAt;
	
	
    
	public VideoStatusRequestDto() {
		super();
	}
	
	public ProcessingStatus getStatus() {
		return status;
	}
	public void setStatus(ProcessingStatus status) {
		this.status = status;
	}
	public String getResultObjectKey() {
		return resultObjectKey;
	}
	public void setResultObjectKey(String resultObjectKey) {
		this.resultObjectKey = resultObjectKey;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public LocalDateTime getProcessedAt() {
		return processedAt;
	}
	public void setProcessedAt(LocalDateTime processedAt) {
		this.processedAt = processedAt;
	}
    
    

}
