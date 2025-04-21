package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository;

import java.util.List;
import java.util.Optional;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;

public interface VideoMetadataRepository {
	
	void save(VideoMetadata metadata);

    Optional<VideoMetadata> findById(String videoId);
    
    List<VideoMetadata> findByUserEmail(String userEmail);
	

}
