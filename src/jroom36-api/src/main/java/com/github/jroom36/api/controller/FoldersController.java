/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.api.controller;

import java.util.Collection;
import java.util.UUID;

import com.github.jroom36.api.dto.CreateFolderView;
import com.github.jroom36.api.dto.FolderView;
import com.github.jroom36.storage.folders.FolderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.jroom36.api.dto.FolderView.makeFoldersView;
import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/v1/folders")
public class FoldersController {
	private final FolderService folderService;

	public FoldersController(FolderService folderService) {
		this.folderService = folderService;
	}

	@GetMapping
	public Collection<FolderView> getFolders() {
		return makeFoldersView(folderService.getFolders());
	}

	@PostMapping(path = {"/{parentFolderId}", ""})
	public Collection<FolderView> createFolder(
			@PathVariable(required = false) UUID parentFolderId,
			@RequestBody CreateFolderView createFolderView) {
		ofNullable(parentFolderId).ifPresentOrElse(
						folderId -> folderService.createFolder(createFolderView.name(), folderId),
						() -> folderService.createFolder(createFolderView.name()));
		return getFolders();
	}
}
