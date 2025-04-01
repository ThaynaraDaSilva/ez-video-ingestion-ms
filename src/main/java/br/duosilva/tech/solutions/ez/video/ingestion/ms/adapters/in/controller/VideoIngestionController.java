package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.in.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoIngestionUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController()
@RequestMapping("/v1/ms/video-ingestion")
@Tag(name = "Video Ingestion Microsservice", description = "Microsserviço responsável por gerenciar os vídeos fornecidos por usuários para processamento.")
public class VideoIngestionController {

	private static final String USER_ID = "6c0dc669-a18e-40d1-93ea-ba328a8daaed";

	private VideoIngestionUseCase videoIngestionUseCase;

	public VideoIngestionController(VideoIngestionUseCase videoIngestionUseCase) {
		super();
		this.videoIngestionUseCase = videoIngestionUseCase;
	}

	@PostMapping(value = "/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadVideo(
			@RequestPart("files") MultipartFile[] multipartFiles,
			@RequestHeader("Authorization") String authorizationHeader
	) {

		String token = authorizationHeader.replace("Bearer ", "");
		DecodedJWT decodedJWT = JWT.decode(token);
		String userId = decodedJWT.getSubject();
		String email = decodedJWT.getClaim("email").asString();

		System.out.println("User ID (sub): " + userId);
		System.out.println("Email: " + email);

		videoIngestionUseCase.ingestVideo(multipartFiles, USER_ID);
		return ResponseEntity.accepted().build();

	}
}
