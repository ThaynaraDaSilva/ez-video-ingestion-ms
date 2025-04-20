package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb.AmazonDynamoDBAdapter;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.http.NotificationHttpClient;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.NotificationRequest;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;

@Component
public class VideoStatusUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoIngestionUseCase.class);
	private final VideoMetadataRepository videoMetadataRepository;
	private final AmazonDynamoDBAdapter amazonDynamoDBAdapter;
	private final NotificationHttpClient notificationHttpClient;

	public VideoStatusUseCase(VideoMetadataRepository videoMetadataRepository,
			AmazonDynamoDBAdapter amazonDynamoDBAdapter, NotificationHttpClient notificationHttpClient) {
		this.videoMetadataRepository = videoMetadataRepository;
		this.amazonDynamoDBAdapter = amazonDynamoDBAdapter;
		this.notificationHttpClient = notificationHttpClient;
	}

	public void updateVideoStatus(String videoId, VideoStatusRequestDto dto) {
		LOGGER.info("############################################################");
		LOGGER.info("#### VIDEO UPDATE PROCESS STARTED ####");
		// 1. Busca o video no repo
		VideoMetadata videoMetadata = null;
		try {

			videoMetadata = videoMetadataRepository.findById(videoId)
					.orElseThrow(() -> new BusinessRuleException("VIDEO NOT FOUND: " + videoId));

			// 2. Atualiza os campos
			videoMetadata.setStatus(dto.getStatus());

			if (dto.getErrorMessage() != null) {
				videoMetadata.setErrorMessage(dto.getErrorMessage());
			} else {
				videoMetadata.setErrorMessage(null);
			}

			videoMetadata.setResultObjectKey(dto.getResultObjectKey());
			videoMetadata.setProcessedAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));

			

			LOGGER.info("#### VIDEO PROCESS FINISHED ####");

			// 4. Se o status for "FAILED", envia notificacao

			if (dto.getStatus() == ProcessingStatus.FAILED) {

				sendFailureNotification(videoMetadata);
				videoMetadata.setNotificationSent(true);
				LOGGER.info("#### NOTIFICATION SENT FOR VIDEO ID: {} ####", videoMetadata.getVideoId());
			}
			
			// 3. Salva novamente no banco
			amazonDynamoDBAdapter.save(videoMetadata);

		} catch (Exception e) {
			LOGGER.info("#### EXCEPTION ####");
			// Envia notificacao em caso de falha ao atualizar o status
			System.out.println("ENTROU NESTA CONDICAO PARA ENVIO########################");
			if (videoMetadata != null) {
				sendFailureNotification(videoMetadata);
			}
			throw new BusinessRuleException("FAILED TO UPDATE VIDEO STATUS: " + e);
		}

	}
	
	private void sendFailureNotification(VideoMetadata videoMetadata) {
		// Ajustar esses parâmetros na instância do NotificationRequest
		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setVideoId(videoMetadata.getVideoId());
		notificationRequest.setStatus("failed");
		notificationRequest.setErrorMessage(videoMetadata.getErrorMessage());
		notificationRequest.setEmail(videoMetadata.getUserEmail());
		try {
			notificationHttpClient.sendNotification(notificationRequest);
			LOGGER.info("Notificação enviada para o Notification Service para o vídeo {}", videoMetadata.getVideoId());
		} catch (Exception e) {
			LOGGER.error("Erro ao enviar notificação para o vídeo {}: {}", videoMetadata.getVideoId(), e.getMessage());
			throw new RuntimeException("Falha ao enviar notificação", e);
		}
	}


}
