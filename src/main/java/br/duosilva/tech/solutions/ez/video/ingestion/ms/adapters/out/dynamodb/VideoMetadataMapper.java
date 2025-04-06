package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.ProcessingStatus;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class VideoMetadataMapper {

	public static Map<String, AttributeValue> toDynamoItem(VideoMetadata metadata) {
		Map<String, AttributeValue> item = new HashMap<>();

		item.put("id", AttributeValue.fromS(metadata.getId()));
		item.put("originalFileName", AttributeValue.fromS(metadata.getOriginalFileName()));
		item.put("contentType", AttributeValue.fromS(metadata.getContentType()));
		item.put("fileSizeBytes", AttributeValue.fromN(String.valueOf(metadata.getFileSizeBytes())));
		item.put("uploadedAt", AttributeValue.fromS(metadata.getUploadedAt().toString()));
		item.put("userId", AttributeValue.fromS(metadata.getUserId()));
		item.put("status", AttributeValue.fromS(metadata.getStatus().name()));

		// Campos opcionais (evitar valores nulos ou vazios)

		if (metadata.getUserEmail() != null && !metadata.getUserEmail().isBlank()) {
			item.put("userEmail", AttributeValue.fromS(metadata.getUserEmail()));
		}

		if (metadata.getVideoDuration() != null) {
			item.put("videoDuration", AttributeValue.fromN(String.valueOf(metadata.getVideoDuration())));
		}

		if (metadata.getErrorMessage() != null && !metadata.getErrorMessage().isBlank()) {
			item.put("errorMessage", AttributeValue.fromS(metadata.getErrorMessage()));
		}

		if (metadata.getResultBucketName() != null && !metadata.getResultBucketName().isBlank()) {
			item.put("resultBucketName", AttributeValue.fromS(metadata.getResultBucketName()));
		}

		if (metadata.getResultObjectKey() != null && !metadata.getResultObjectKey().isBlank()) {
			item.put("resultObjectKey", AttributeValue.fromS(metadata.getResultObjectKey()));
		}

		if (metadata.getProcessedAt() != null) {
			item.put("processedAt", AttributeValue.fromS(metadata.getProcessedAt().toString()));
		}

		return item;
	}

	public static VideoMetadata fromDynamoItem(Map<String, AttributeValue> item) {
		return new VideoMetadata(item.get("id").s(), item.get("originalFileName").s(), item.get("contentType").s(),
				Long.parseLong(item.get("fileSizeBytes").n()),
				item.containsKey("videoDuration") ? Long.parseLong(item.get("videoDuration").n()) : null,
				item.get("userId").s(), item.get("userEmail").s(),
				Enum.valueOf(ProcessingStatus.class, item.get("status").s()),
				item.containsKey("errorMessage") ? item.get("errorMessage").s() : null,
				item.containsKey("resultBucketName") ? item.get("resultBucketName").s() : null,
				item.containsKey("resultObjectKey") ? item.get("resultObjectKey").s() : null,
				item.containsKey("processedAt") ? LocalDateTime.parse(item.get("processedAt").s()) : null);
	}
}
