package br.duosilva.tech.solutions.ez.video.ingestion.ms.adapters.in.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.dto.VideoStatusRequestDto;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoIngestionUseCase;
import br.duosilva.tech.solutions.ez.video.ingestion.ms.application.usecases.VideoStatusUseCase;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/v1/ms/video-ingestion")
@Tag(name = "Video Ingestion Microsservice", description = "Microsserviço responsável por gerenciar os vídeos fornecidos por usuários para processamento.")
public class VideoIngestionController {

	private static final String USER_ID = "6c0dc669-a18e-40d1-93ea-ba328a8daaed";
	private static final String USER_EMAIL = "thaynara-r@hotmail.com";

	private VideoIngestionUseCase videoIngestionUseCase;
	private VideoStatusUseCase videoStatusUseCase;

	public VideoIngestionController(VideoIngestionUseCase videoIngestionUseCase, VideoStatusUseCase videoStatusUseCase) {
		super();
		this.videoIngestionUseCase = videoIngestionUseCase;
		this.videoStatusUseCase = videoStatusUseCase;
	}

	@PostMapping(value = "/videos/upload-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> uploadVideo(@RequestPart("files") MultipartFile[] multipartFiles) {

		videoIngestionUseCase.ingestVideo(multipartFiles, USER_ID, USER_EMAIL);
		return ResponseEntity.accepted().build();

	}
	
	@PutMapping("/videos/{videoId}/update-status")
	public ResponseEntity<Void> updateVideoStatus(@PathVariable String videoId,
	                                              @RequestBody @Valid VideoStatusRequestDto requestDto) {
	    videoStatusUseCase.updateVideoStatus(videoId, requestDto);
	    return ResponseEntity.noContent().build();
	}

}
