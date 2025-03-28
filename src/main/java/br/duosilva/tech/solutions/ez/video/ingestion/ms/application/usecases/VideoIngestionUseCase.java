package br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases;

import java.time.Duration;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.s3.AmazonS3Adapter;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.service.VideoUploadPolicyService;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.ErrorMessages;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.s3.S3KeyGenerator;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.util.DurationUtils;

/**
 * Use case responsável por orquestrar o processo de upload e processamento de
 * vídeos enviados pelo usuário. Aplica regras de negócio e aciona os serviços
 * responsáveis pelo processamento técnico.
 */

@Component
public class VideoIngestionUseCase {

	private static final Logger LOGGER = LoggerFactory.getLogger(VideoIngestionUseCase.class);

	private final VideoUploadPolicyService videoUploadPolicyService;
	private final AmazonS3Adapter amazonS3Adapter;

	public VideoIngestionUseCase(VideoUploadPolicyService videoUploadPolicyService, AmazonS3Adapter amazonS3Adapter) {
	
		this.videoUploadPolicyService = videoUploadPolicyService;
		this.amazonS3Adapter = amazonS3Adapter;
	}

	/**
	 * Processa o upload de um ou mais vídeos enviados pelo usuário. Aplica as
	 * regras de negócio (como tamanho do vídeo e limite diário de uploads) e
	 * executa o processamento técnico para extração de frames.
	 *
	 * @param multipartFiles Array de arquivos de vídeo enviados no upload
	 * @param userId         ID do usuário que está realizando o upload
	 * @throws BusinessRuleException se não houver vídeos ou se alguma regra de
	 *                               negócio for violada
	 */
	public void ingestVideo(MultipartFile[] multipartFiles, String userId) {

		this.validateFilesPresence(multipartFiles);

		videoUploadPolicyService.validateUserDailyUploadLimit(userId);

		for (MultipartFile file : multipartFiles) {
			if (file.isEmpty())
				continue;

			this.ingestSingleVideoFile(file, userId);
		}

	}

	private void validateFilesPresence(MultipartFile[] files) {
		if (files == null || files.length == 0) {
			throw new BusinessRuleException(ErrorMessages.NO_VIDEO_PROVIDED);
		}
	}

	private void ingestSingleVideoFile(MultipartFile file, String userId) {
		long startTime = System.currentTimeMillis();
		LOGGER.info("############################################################");
		LOGGER.info("#### VIDEO PROCESSING STARTED: {} ####", file.getOriginalFilename());

		videoUploadPolicyService.validateFileSize(file);

		try {
			
			// 1. Gera o videoId
		    String videoId = UUID.randomUUID().toString();
		    
		    // 2. Gera a chave S3 com base no userId, videoId e extensão
		    String s3Key = S3KeyGenerator.generateS3Key(userId, file, videoId);
		   

			if (amazonS3Adapter.doesFileExistInS3(s3Key)) {
				LOGGER.warn("#### FILE ALREADY EXISTS IN S3: {} — SKIPPING UPLOAD ####", s3Key);
			} else {
				amazonS3Adapter.uploadFileToS3(s3Key, file);
				LOGGER.info("#### FILE UPLOADED TO S3: {} ####", s3Key);
			}

			String presignedUrl = amazonS3Adapter.generatePresignedUrl(s3Key, Duration.ofMinutes(15));
			LOGGER.info("#### PRESIGNED URL: {} ####", presignedUrl);
			LOGGER.info("#### PRESIGNED URL VALID FOR {} MINUTES ####",  Duration.ofMinutes(15));

		} catch (Exception e) {
			throw new BusinessRuleException("Failed to process video: " + e);
		} finally {
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			LOGGER.info("#### VIDEO PROCESSING COMPLETED: {} ####", file.getOriginalFilename());
			LOGGER.info("#### TOTAL PROCESSING TIME: {} ####", DurationUtils.formatDuration(duration));
		}
	}

	

}
