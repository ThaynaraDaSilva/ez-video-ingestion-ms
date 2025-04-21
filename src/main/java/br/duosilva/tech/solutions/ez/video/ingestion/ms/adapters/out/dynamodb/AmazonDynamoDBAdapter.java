package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence.VideoMetadataEntity;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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

	@Override
	public List<VideoMetadata> findByUserEmail(String userEmail) {
		AttributeValue emailAttribute = AttributeValue.builder().s(userEmail).build();

		Expression filterExpression = Expression.builder().expression("userEmail = :userEmail")
				.expressionValues(Map.of(":userEmail", emailAttribute)).build();

		SdkIterable<Page<VideoMetadataEntity>> results = videoMetadataDynamoTable
				.scan(r -> r.filterExpression(filterExpression));

		List<VideoMetadataEntity> entities = new ArrayList<>();
		results.forEach(page -> entities.addAll(page.items()));

		return entities.stream().map(VideoMetadataMapper::toDomain).collect(Collectors.toList());
	}

}
