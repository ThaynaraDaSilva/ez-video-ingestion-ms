package br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AmazonSQSConfig {
	
	 private final AmazonProperties amazonProperties;

	    public AmazonSQSConfig(AmazonProperties amazonProperties) {
	        this.amazonProperties = amazonProperties;
	       
	    }
	    
	   /* @Bean
	    public SqsClient sqsClient() {
	        SqsClientBuilder builder = SqsClient.builder()
	            .region(Region.of(amazonProperties.getRegion()))
	            .credentialsProvider(
	                StaticCredentialsProvider.create(
	                    AwsBasicCredentials.create(
	                        amazonProperties.getCredentials().getAccessKey(),
	                        amazonProperties.getCredentials().getSecretKey()
	                    )
	                )
	            );

	        String endpoint = amazonProperties.getSqs().getEndpoint();

	        if (endpoint != null && !endpoint.isBlank()) {
	            builder.endpointOverride(URI.create(endpoint));
	        }

	        return builder.build();
	    }*/
	    
	    @Bean
	    public SqsClient sqsClient() {
	        return SqsClient.builder()
	                .region(Region.of(amazonProperties.getRegion()))
	                .credentialsProvider(DefaultCredentialsProvider.create())
	                .build();
	    }

}
