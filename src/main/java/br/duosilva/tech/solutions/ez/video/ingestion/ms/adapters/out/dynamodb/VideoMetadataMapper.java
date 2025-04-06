package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
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
        entity.setStatus(domain.getStatus().name());
        entity.setErrorMessage(domain.getErrorMessage());
        entity.setResultBucketName(domain.getResultBucketName());
        entity.setResultObjectKey(domain.getResultObjectKey());
        entity.setProcessedAt(domain.getProcessedAt());
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
            ProcessingStatus.valueOf(entity.getStatus()),
            entity.getErrorMessage(),
            entity.getResultBucketName(),
            entity.getResultObjectKey(),
            entity.getProcessedAt()
        );
    }
}
