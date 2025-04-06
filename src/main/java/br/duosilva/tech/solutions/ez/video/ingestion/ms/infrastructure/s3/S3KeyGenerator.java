package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.s3;

import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.frameworks.exception.BusinessRuleException;

public class S3KeyGenerator {

	public static String generateS3Key(String userId, MultipartFile file, String videoId) {
		String originalFilename = file.getOriginalFilename();

		if (originalFilename == null || !originalFilename.contains(".")) {
			throw new BusinessRuleException("INVALID FILE NAME FOR S3 STORAGE: ORIGINAL FILE NAME IS NULL OR HAS NO EXTENSION");
		}

		
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String baseName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
	
		    return String.format("%s/%s-%s%s", userId, videoId, baseName, extension);
	}

}
