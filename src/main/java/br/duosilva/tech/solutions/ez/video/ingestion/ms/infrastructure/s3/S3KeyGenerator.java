package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.s3;

import org.springframework.web.multipart.MultipartFile;

public class S3KeyGenerator {

	public static String generateS3Key(String userId, MultipartFile file, String videoId) {
		String originalFilename = file.getOriginalFilename();
		String extension = "";

		if (originalFilename != null && originalFilename.contains(".")) {
			extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		}

		return String.format("%s/%s%s", userId, videoId, extension);
	}

}
