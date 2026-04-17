package com.github.jroom36.api.controller;

import java.io.IOException;
import java.util.UUID;

import com.github.jroom36.api.dto.FileInfoView;
import com.github.jroom36.storage.files.FileInfo;
import com.github.jroom36.storage.files.FilesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FilesController {
	private final FilesService filesService;

	@PostMapping("/upload")
	public FileInfoView upload(@RequestParam("file") @NotNull MultipartFile file,
			@RequestParam(value = "folderId", required = false) UUID folderId) throws IOException {
		FileInfo fileInfo = filesService.uploadFile(file.getOriginalFilename(), file.getInputStream(), folderId);
		return new FileInfoView(fileInfo);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable UUID id) {
		FileInfo fileInfo = filesService.getFileInfo(id);

		StreamingResponseBody stream = os -> filesService.sendTo(id, os);

		return ResponseEntity.ok()
				.contentType(APPLICATION_OCTET_STREAM)
				.header(CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(fileInfo.name()))
				.body(stream);
	}

	@GetMapping
	public Page<FileInfoView> getFiles(Pageable pageable) {
		return FileInfoView.makePageView(filesService.getFiles(pageable));
	}
}
