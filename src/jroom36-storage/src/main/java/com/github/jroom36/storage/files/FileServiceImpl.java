/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.UUID;

import com.github.jroom36.storage.files.reposiotry.FileEntity;
import com.github.jroom36.storage.files.reposiotry.FilesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.cleanPath;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FilesService {
	private final FilesRepository filesRepository;

	@Override
	public FileInfo uploadFile(String name, InputStream is, UUID folderId) throws IOException {
		FileEntity fileEntity = new FileEntity()
				.setFolderId(folderId)
				.setName(cleanPath(requireNonNull(name)))
				.setSizeBytes((long) is.available())
				.setContent(Hibernate.getLobHelper().createBlob(is, is.available()));
		return filesRepository.save(fileEntity).toRecord();
	}

	@Override
	public FileInfo getFileInfo(UUID id) {
		return filesRepository.findById(id).orElseThrow(EntityNotFoundException::new).toRecord();
	}

	@Override
	public Page<FileInfo> getFiles(Pageable pageable) {
		return filesRepository.findAll(pageable).map(FileEntity::toRecord);
	}

	@Override
	public void sendTo(UUID fileId, OutputStream os) throws IOException {
		FileEntity entity = filesRepository.findById(fileId)
				.orElseThrow(EntityNotFoundException::new);

		try (InputStream is = entity.getContent().getBinaryStream()) {
			is.transferTo(os);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
}
