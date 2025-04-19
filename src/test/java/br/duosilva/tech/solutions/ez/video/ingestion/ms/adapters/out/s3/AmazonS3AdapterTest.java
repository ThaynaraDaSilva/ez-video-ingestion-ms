package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.out.s3;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.infrastructure.config.AmazonProperties.S3;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

public class AmazonS3AdapterTest {

    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private AmazonProperties properties;
    private AmazonS3Adapter amazonS3Adapter;

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        s3Presigner = mock(S3Presigner.class);

        properties = mock(AmazonProperties.class);
        S3 s3Config = mock(S3.class);

        when(properties.getS3()).thenReturn(s3Config);
        when(s3Config.getBucketName()).thenReturn("test-bucket");

        amazonS3Adapter = new AmazonS3Adapter(s3Client, properties, s3Presigner);
    }

    @Test
    void testUploadFileToS3_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("test content".getBytes()));
        when(mockFile.getSize()).thenReturn(12L);
        when(mockFile.getContentType()).thenReturn("application/zip");

        amazonS3Adapter.uploadFileToS3("user123/video.zip", mockFile);

        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testUploadFileToS3_IOException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenThrow(new IOException("File read error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                amazonS3Adapter.uploadFileToS3("user123/video.zip", mockFile));

        assertEquals("Failed to upload file to S3", exception.getMessage());
    }

    @Test
    void testGeneratePresignedUrl() {
        URL fakeUrl = mock(URL.class);
        when(fakeUrl.toString()).thenReturn("https://s3.amazonaws.com/test-bucket/user123/video.zip");

        PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
        when(presignedRequest.url()).thenReturn(fakeUrl);

        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedRequest);

        String result = amazonS3Adapter.generatePresignedUrl("user123/video.zip", Duration.ofMinutes(5));

        assertEquals("https://s3.amazonaws.com/test-bucket/user123/video.zip", result);
    }

    @Test
    void testDoesFileExistInS3_Exists() {
        S3Object s3Object = S3Object.builder().key("user123/video.zip").build();

        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(List.of(s3Object))
                .build();

        when(s3Client.listObjectsV2(any(java.util.function.Consumer.class))).thenReturn(response);

        boolean exists = amazonS3Adapter.doesFileExistInS3("user123/video.zip");

        assertTrue(exists);
    }

    @Test
    void testDoesFileExistInS3_NotExists() {
        ListObjectsV2Response response = ListObjectsV2Response.builder()
                .contents(List.of())
                .build();

        when(s3Client.listObjectsV2(any(java.util.function.Consumer.class))).thenReturn(response);

        boolean exists = amazonS3Adapter.doesFileExistInS3("nonexistent/file.zip");

        assertFalse(exists);
    }

    @Test
    void testGetBucketName() {
        String bucket = amazonS3Adapter.getBucketName();
        assertEquals("test-bucket", bucket);
    }
}
