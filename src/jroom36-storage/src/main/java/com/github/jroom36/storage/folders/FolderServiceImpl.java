/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.jroom36.storage.folders.repository.FolderEntity;
import com.github.jroom36.storage.folders.repository.FoldersRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderServiceImpl implements FolderService {
	private final FoldersRepository foldersRepository;

	@Override
	public Collection<Folder> buildFoldersTree() {
		List<FolderEntity> allFolders = foldersRepository.findAll();
		Map<UUID, Folder> foldersMap = buildFoldersMap(allFolders);

		allFolders.stream().filter(FolderEntity::hasParentFolder).forEach(entity ->
				foldersMap.get(entity.getParentFolderId()).nestedFolders().add(foldersMap.get(entity.getId())));

		return allFolders.stream().filter(FolderEntity::isTopLevelFolder).map(
				entity -> foldersMap.get(entity.getId())).collect(toList());
	}

	private Map<UUID, Folder> buildFoldersMap(Collection<FolderEntity> allFolders) {
		return allFolders.stream().collect(toMap(FolderEntity::getId, e ->
				new Folder(e.getId(), e.getName(), e.getParentFolderId(), new ArrayList<>())));
	}

	@Override
	public void createFolder(String name) {
		foldersRepository.save(new FolderEntity().setName(name)).toRecord();
	}

	@Override
	public void createFolder(String name, @NotNull UUID parentFolderId) {
		foldersRepository.save(new FolderEntity().setName(name).setParentFolderId(parentFolderId)).toRecord();
	}
}
