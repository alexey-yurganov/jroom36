/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Collections.synchronizedList;
import static java.util.stream.Collectors.toList;

@Service
public class FileServiceImpl implements FilesService{
	private static final Collection<FileInfo> FILES = synchronizedList(new ArrayList<>());

	@Override
	public FileInfo uploadFile(String name, long sizeBytes, InputStream inputStream) {
		FileInfo fileInfo = new FileInfo(UUID.randomUUID(), name, sizeBytes);
		FILES.add(fileInfo);
		return fileInfo;
	}

	@Override
	public Page<FileInfo> getFiles(Pageable pageable) {
		return new PageImpl<>(FILES.stream().skip(pageable.getOffset()).limit(pageable.getPageSize())
				.collect(toList()),pageable, FILES.size());
	}
}
