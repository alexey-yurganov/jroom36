/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import static java.util.Collections.synchronizedList;
import static java.util.UUID.randomUUID;

@Service
public class FolderServiceImpl implements FolderService {
	private static final Map<UUID, Folder> FOLDERS_BY_ID = new ConcurrentHashMap<>();

	@Override
	public Collection<Folder> getFolders() {
		return FOLDERS_BY_ID.values();
	}

	@Override
	public void createFolder(String name) {
		Folder folder = makeFolder(name);
		FOLDERS_BY_ID.put(folder.id(), folder);
	}

	@Override
	public void createFolder(String name, @NotNull UUID parentFolderId) {
		FOLDERS_BY_ID.get(parentFolderId).nestedFolders().add(makeFolder(name));
	}

	private Folder makeFolder(String name) {
		return new Folder(randomUUID(), name, synchronizedList(new ArrayList<>()));
	}
}
