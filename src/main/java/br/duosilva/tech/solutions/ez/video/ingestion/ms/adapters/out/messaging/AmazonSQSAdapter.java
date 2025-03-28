package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoIngestionMessage;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.retry.annotation.Backoff;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class AmazonSQSAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonSQSAdapter.class);

	private final SqsClient sqsClient;
	private final AmazonProperties amazonProperties;
	private final ObjectMapper objectMapper;

	public AmazonSQSAdapter(SqsClient sqsClient, AmazonProperties amazonProperties, ObjectMapper objectMapper) {
		this.sqsClient = sqsClient;
		this.amazonProperties = amazonProperties;
		this.objectMapper = objectMapper;
	}

	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public void publishVideoIngestionMessage(VideoIngestionMessage message) {
		try {
			String messageBody = objectMapper.writeValueAsString(message);
			String queueUrl = resolveQueueUrl();

			SendMessageRequest request = SendMessageRequest.builder().queueUrl(queueUrl).messageBody(messageBody)
					.build();

			sqsClient.sendMessage(request);

			LOGGER.info("VIDEO_INGESTION_MESSAGE PUBLISHED TO SQS: {}", messageBody);

		} catch (JsonProcessingException e) {
			LOGGER.error("FAILED TO SERIALIZE VIDEO_INGESTION_MESSAGE", e);
			throw new RuntimeException("Failed to serialize VideoIngestionMessage", e);
		} catch (Exception e) {
			LOGGER.error("FAILED TO PUBLISH VIDEO_INGESTION_MESSAGE TO SQS", e);
			throw new RuntimeException("Failed to publish VideoIngestionMessage to SQS", e);
		}
	}

	private String resolveQueueUrl() {
		String endpoint = amazonProperties.getSqs().getEndpoint();
		String queueName = amazonProperties.getSqs().getQueueName();

		// Ex: http://localhost:4566/000000000000/queue-name
		return String.format("%s/000000000000/%s", endpoint, queueName);
	}

}
