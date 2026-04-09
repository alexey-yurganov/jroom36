package com.github.jroom36.api.controller;

import java.io.IOException;

import com.github.jroom36.api.dto.FileInfoView;
import com.github.jroom36.storage.FileInfo;
import com.github.jroom36.storage.FilesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

	private final FilesService filesService;

	public FileUploadController(FilesService filesService) {
		this.filesService = filesService;
	}

	@PostMapping("/upload")
	public FileInfoView upload(@RequestParam("file") @NotNull MultipartFile file) throws IOException {
		FileInfo fileInfo = filesService.uploadFile(file.getName(), file.getSize(), file.getInputStream());
		return new FileInfoView(fileInfo);
	}

	@GetMapping
	public Page<FileInfoView> getFiles(Pageable pageable) {
		return FileInfoView.makePageView(filesService.getFiles(pageable));
	}
}
