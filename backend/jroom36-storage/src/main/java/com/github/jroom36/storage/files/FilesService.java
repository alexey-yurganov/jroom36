/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilesService {
	FileInfo uploadFile(String name, InputStream inputStream, UUID folderId) throws IOException;
	FileInfo getFileInfo(UUID id);
	Page<FileInfo> getFiles(Pageable pageable);
	void sendTo(UUID fileId, OutputStream os) throws IOException;
}
