/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.api.dto;

import java.util.UUID;

import com.github.jroom36.storage.files.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import static java.util.stream.Collectors.toList;

public record FileInfoView(UUID id, String name, Long sizeBytes) {
	public FileInfoView(FileInfo fileInfo) {
		this(fileInfo.id(), fileInfo.name(), fileInfo.sizeBytes());
	}
	public static Page<FileInfoView> makePageView(Page<FileInfo> files) {
		return new PageImpl<>(
				files.stream().map(FileInfoView::new).collect(toList()), files.getPageable(), files.getSize());
	}
}
