package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.repository.VideoMetadataRepository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Repository
public class DynamoVideoMetadataRepository implements VideoMetadataRepository {
	
	private final DynamoDbTable<VideoMetadata> dynamoTable;
	
	public DynamoVideoMetadataRepository(DynamoDbTable<VideoMetadata> dynamoTable) {
        this.dynamoTable = dynamoTable;
    }

	@Override
	public void save(VideoMetadata metadata) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<VideoMetadata> findById(String videoId) {
		 VideoMetadata item = dynamoTable.getItem(r -> r.key(k -> k.partitionValue(videoId)));
	        return Optional.ofNullable(item);
	}

}
