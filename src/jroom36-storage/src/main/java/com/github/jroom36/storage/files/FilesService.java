/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.io.InputStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilesService {
	FileInfo uploadFile(String name, long sizeBytes, InputStream inputStream);
	Page<FileInfo> getFiles(Pageable pageable);
}
