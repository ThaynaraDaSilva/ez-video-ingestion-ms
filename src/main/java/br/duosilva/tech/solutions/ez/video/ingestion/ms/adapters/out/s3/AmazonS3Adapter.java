package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.s3;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
public class AmazonS3Adapter {

	private final S3Client s3Client;
	private final AmazonProperties properties;
	private final S3Presigner s3Presigner;

	public AmazonS3Adapter(S3Client s3Client, AmazonProperties properties, S3Presigner s3Presigner) {
		this.s3Client = s3Client;
		this.properties = properties;
		this.s3Presigner = s3Presigner;
	}

	/**
	 * Uploads a .zip file to the configured S3 bucket.
	 *
	 * @param key     the object key (ex: userId/filename.zip)
	 * @param zipFile the zip file to upload
	 */
	/*
	 * public void uploadFileToS3(String key, File zipFile) { String bucketName =
	 * properties.getS3().getBucketName();
	 * 
	 * PutObjectRequest request =
	 * PutObjectRequest.builder().bucket(bucketName).key(key).contentType(
	 * "application/zip") .build();
	 * 
	 * s3Client.putObject(request, RequestBody.fromFile(zipFile.toPath())); }
	 */
	
	public void uploadFileToS3(String key, MultipartFile multipartFile) {
	    String bucketName = properties.getS3().getBucketName();

	    try {
	        PutObjectRequest request = PutObjectRequest.builder()
	                .bucket(bucketName)
	                .key(key)
	                .contentType(multipartFile.getContentType())
	                .build();

	        s3Client.putObject(request, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
	    } catch (IOException e) {
	        throw new RuntimeException("Failed to upload file to S3", e);
	    }
	}

	public String generatePresignedUrl(String objectKey, Duration expiration) {
		String bucketName = properties.getS3().getBucketName();

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().getObjectRequest(getObjectRequest)
				.signatureDuration(expiration).build();

		PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

		return presignedRequest.url().toString();
	}

	/**
	 * Checks if the zip file already exists in the bucket.
	 *
	 * @param key object key
	 * @return true if object exists
	 */
	public boolean doesFileExistInS3(String key) {
		String bucketName = properties.getS3().getBucketName();
		return s3Client.listObjectsV2(builder -> builder.bucket(bucketName).prefix(key)).contents().stream()
				.anyMatch(obj -> obj.key().equals(key));
	}

}
