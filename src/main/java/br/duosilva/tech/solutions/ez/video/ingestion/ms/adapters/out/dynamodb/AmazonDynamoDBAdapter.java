package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.dynamodb;

import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Component
public class AmazonDynamoDBAdapter implements VideoMetadataRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonDynamoDBAdapter.class);

	private final DynamoDbClient dynamoDbClient;
	private final AmazonProperties amazonProperties;
	

	public AmazonDynamoDBAdapter(DynamoDbClient dynamoDbClient, AmazonProperties amazonProperties) {
		this.dynamoDbClient = dynamoDbClient;
		this.amazonProperties = amazonProperties;
	}
	
	
	@Override
    public void save(VideoMetadata metadata) {
        Map<String, AttributeValue> item = VideoMetadataMapper.toDynamoItem(metadata);

        PutItemRequest request = PutItemRequest.builder()
                .tableName(amazonProperties.getDynamodb().getTableName())
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
        LOGGER.debug("Saving video metadata for videoId={} and userId={}", metadata.getId(), metadata.getUserId());
    }


	@Override
	public Optional<VideoMetadata> findById(String videoId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	/*
	 * @Override public Optional<VideoMetadata> findById(String userId, String
	 * videoId) { Map<String, AttributeValue> key = Map.of( "userId",
	 * AttributeValue.fromS(userId), "id", AttributeValue.fromS(videoId) );
	 * 
	 * GetItemRequest request = GetItemRequest.builder()
	 * .tableName(amazonProperties.getDynamodb().getTableName()) .key(key) .build();
	 * 
	 * var response = dynamoDbClient.getItem(request); if (!response.hasItem()) {
	 * return Optional.empty(); }
	 * 
	 * return Optional.of(VideoMetadataMapper.fromDynamoItem(response.item())); }
	 */
}
