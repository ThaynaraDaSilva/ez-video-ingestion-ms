package br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.policy;

import org.springframework.stereotype.Component;

@Component
public class VideoUploadPolicy {
	
	/**
     * Define o intervalo minimo entre frames que devem ser extraidos do video.
     * Exemplo: 1000 = 1 frame por segundo
     */
    public long getFrameExtractionIntervalInMillis() {
        return 1000L; // 1 segundo
    }

	public long getMaxFileSizeBytes() {
		return 50L * 1024 * 1024;
	}

	public long getMaxTotalRequestSizeBytes() {
		return 100L * 1024 * 1024;
	}

	public int getMaxFilesPerRequest() {
		return 3;
	}

	public int getMaxUploadsPerDay() {
		return 5;
	}

	public long getMaxDailyTotalSizeBytes() {
		return 300L* 1024 * 1024;
	}

}
