package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusResponseDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence.VideoMetadataEntity;

public class VideoMetadataMapper {

	public static VideoMetadataEntity toEntity(VideoMetadata domain) {
		
        VideoMetadataEntity entity = new VideoMetadataEntity();
        entity.setVideoId(domain.getVideoId());
        entity.setOriginalFileName(domain.getOriginalFileName());
        entity.setContentType(domain.getContentType());
        entity.setFileSizeBytes(domain.getFileSizeBytes());
        entity.setVideoDuration(domain.getVideoDuration());
        entity.setUserId(domain.getUserId());
        entity.setUserEmail(domain.getUserEmail());
        entity.setStatus(domain.getStatus());
        entity.setErrorMessage(domain.getErrorMessage());
        entity.setResultBucketName(domain.getResultBucketName());
        entity.setResultObjectKey(domain.getResultObjectKey());
        entity.setProcessedAt(domain.getProcessedAt());
        entity.setNotificationSent(domain.isNotificationSent());
        return entity;
    }

    public static VideoMetadata toDomain(VideoMetadataEntity entity) {
        return new VideoMetadata(
            entity.getVideoId(),
            entity.getOriginalFileName(),
            entity.getContentType(),
            entity.getFileSizeBytes(),
            entity.getVideoDuration(),
            entity.getUserId(),
            entity.getUserEmail(),
            entity.getStatus(),
            entity.getErrorMessage(),
            entity.getResultBucketName(),
            entity.getResultObjectKey(),
            entity.getProcessedAt()
        );
    }
    
    public static VideoStatusResponseDto toDto(VideoMetadata metadata) {
        return new VideoStatusResponseDto(
                metadata.getVideoId(),
                metadata.getOriginalFileName(),
                metadata.getProcessedAt(),
                metadata.getResultObjectKey(),
                metadata.getStatus(),
                metadata.getUserEmail()
        );
    }
}
