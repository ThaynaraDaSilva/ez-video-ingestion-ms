package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto;

import java.time.LocalDateTime;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

public class VideoStatusUpdateRequestDto {
	
	private ProcessingStatus status;
    private String resultObjectKey;
    private String errorMessage;
    private LocalDateTime processedAt;
    
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
