package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusUpdateRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;

public class VideoStatusUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoIngestionUseCase.class);
	private final VideoMetadataRepository videoMetadataRepository;

	public VideoStatusUseCase(VideoMetadataRepository videoMetadataRepository) {
		this.videoMetadataRepository = videoMetadataRepository;
	}

	public void updateVideoStatus(String videoId, VideoStatusUpdateRequestDto dto) {
		LOGGER.info("############################################################");
		LOGGER.info("#### VIDEO UPDATE PROCESS STARTED ####");
		// 1. Busca o video no repo
		try {

			VideoMetadata videoMetadata = videoMetadataRepository.findById(videoId)
					.orElseThrow(() -> new BusinessRuleException("VIDEO NOT FOUND: " + videoId));


			// 2. Atualiza os campos
			videoMetadata.setStatus(dto.getStatus());
			videoMetadata.setErrorMessage(dto.getErrorMessage());
			videoMetadata.setResultObjectKey(dto.getResultObjectKey());
			videoMetadata.setProcessedAt(dto.getProcessedAt());

			// 3. Salva novamente no banco
			videoMetadataRepository.save(videoMetadata);
		} catch (Exception e) {

		}

	}

}
