package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence.VideoMetadataEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class AmazonDynamoDBAdapter implements VideoMetadataRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonDynamoDBAdapter.class);


	private final DynamoDbTable<VideoMetadataEntity> videoMetadataDynamoTable;

	public AmazonDynamoDBAdapter(DynamoDbTable<VideoMetadataEntity> videoMetadataDynamoTable) {

		this.videoMetadataDynamoTable = videoMetadataDynamoTable;
	}

	@Override
	public void save(VideoMetadata metadata) {
		VideoMetadataEntity entity = VideoMetadataMapper.toEntity(metadata);
		videoMetadataDynamoTable.putItem(entity);

	}

	@Override
	public Optional<VideoMetadata> findById(String videoId) {
		VideoMetadataEntity entity = videoMetadataDynamoTable.getItem(r -> r.key(k -> k.partitionValue(videoId)));

		if (entity == null) {
			return Optional.empty();
		}

		return Optional.of(VideoMetadataMapper.toDomain(entity));
	}

}
