package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.persistence.VideoMetadataEntity;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
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

   /* @Bean
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
    }*/
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(amazonProperties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build(); // Nao define creds e nem endpoint
    }
    
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
    
    @Bean
    public DynamoDbTable<VideoMetadataEntity> videoMetadataTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table("video_metadata", TableSchema.fromBean(VideoMetadataEntity.class));
    }

}
