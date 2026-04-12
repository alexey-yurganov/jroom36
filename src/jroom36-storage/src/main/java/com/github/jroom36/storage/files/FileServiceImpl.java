/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.cleanPath;

@Service
public class FileServiceImpl implements FilesService{
	private static final Map<UUID, FileInfo> FILES = new ConcurrentHashMap<>();
	private static final Map<UUID, byte[]> CONTENT = new ConcurrentHashMap<>();

	@Override
	public FileInfo uploadFile(String name, long sizeBytes, InputStream is) throws IOException {
		String safeName = cleanPath(requireNonNull(name));
		FileInfo fileInfo = new FileInfo(randomUUID(), safeName, sizeBytes);

		try(InputStream inputStream = is) {
			CONTENT.put(fileInfo.id(), inputStream.readAllBytes());
		}

		FILES.put(fileInfo.id(), fileInfo);
		return fileInfo;
	}

	@Override
	public FileInfo getFileInfo(UUID id) {
		return FILES.get(id);
	}

	@Override
	public Page<FileInfo> getFiles(Pageable pageable) {
		return new PageImpl<>(FILES.values().stream().skip(pageable.getOffset()).limit(pageable.getPageSize())
				.collect(toList()),pageable, FILES.size());
	}

	@Override
	public void sendTo(UUID fileId, OutputStream os) throws IOException {
		byte[] content = CONTENT.get(fileId);
		os.write(content);
	}
}
