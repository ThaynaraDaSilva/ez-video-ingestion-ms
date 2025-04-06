package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.domain.model.VideoMetadata;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;



@Configuration
public class AmazonDynamoDbConfig {
	
	private final AmazonProperties amazonProperties;

    public AmazonDynamoDbConfig(AmazonProperties amazonProperties) {
        this.amazonProperties = amazonProperties;
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        var builder = DynamoDbClient.builder()
                .region(Region.of(amazonProperties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        amazonProperties.getCredentials().getAccessKey(),
                                        amazonProperties.getCredentials().getSecretKey()
                                )
                        )
                );

        String endpoint = amazonProperties.getDynamodb().getEndpoint();
        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }
    
    @Bean
    public DynamoDbTable<VideoMetadata> videoMetadataTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("video_metadata", TableSchema.fromBean(VideoMetadata.class));
    }

}
