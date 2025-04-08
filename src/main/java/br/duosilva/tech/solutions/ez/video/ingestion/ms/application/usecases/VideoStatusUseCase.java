package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb.AmazonDynamoDBAdapter;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;

@Component
public class VideoStatusUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoIngestionUseCase.class);
	private final VideoMetadataRepository videoMetadataRepository;
	private final AmazonDynamoDBAdapter amazonDynamoDBAdapter;

	public VideoStatusUseCase(VideoMetadataRepository videoMetadataRepository, AmazonDynamoDBAdapter amazonDynamoDBAdapter ) {
		this.videoMetadataRepository = videoMetadataRepository;
		this.amazonDynamoDBAdapter  = amazonDynamoDBAdapter;  
	}

	public void updateVideoStatus(String videoId, VideoStatusRequestDto dto) {
		LOGGER.info("############################################################");
		LOGGER.info("#### VIDEO UPDATE PROCESS STARTED ####");
		// 1. Busca o video no repo
		try {

			VideoMetadata videoMetadata = videoMetadataRepository.findById(videoId)
					.orElseThrow(() -> new BusinessRuleException("VIDEO NOT FOUND: " + videoId));


			// 2. Atualiza os campos
			videoMetadata.setStatus(dto.getStatus());
			System.out.println("PROCESSED AT: "+ dto.getProcessedAt());
			System.out.println("STATUS 0 NO USE CASE: "+ dto.getStatus());
			System.out.println("STATUS 1  NO USE CASE: "+ videoMetadata.getStatus());
			if(dto.getErrorMessage()!= null) {
				videoMetadata.setErrorMessage(dto.getErrorMessage());
			}else {
				videoMetadata.setErrorMessage(null);
			}
			
			videoMetadata.setResultObjectKey(dto.getResultObjectKey());
			videoMetadata.setProcessedAt(dto.getProcessedAt());

			// 3. Salva novamente no banco
			amazonDynamoDBAdapter.save(videoMetadata);
			
			LOGGER.info("#### VIDEO PROCESS FINISHED ####");
			
		} catch (Exception e) {
			LOGGER.info("#### EXCEPTION ####");
			throw new BusinessRuleException("FAILED TO UPDATE VIDEO STATUS: " + e);
		}

	}

}
