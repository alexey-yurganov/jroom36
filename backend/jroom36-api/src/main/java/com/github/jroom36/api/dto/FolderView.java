/* (c) 2026 alexey-yurganov, MIT License */
package com.github.jroom36.api.dto;

import java.util.Collection;
import java.util.UUID;

import com.github.jroom36.storage.folders.Folder;

import static java.util.stream.Collectors.toList;

public record FolderView(UUID id, String name, Collection<FolderView> nestedFolders){

	public static Collection<FolderView> makeFoldersView(Collection<Folder> folders) {
		return folders.stream().map(f ->
				new FolderView(f.id(), f.name(), makeFoldersView(f.nestedFolders()))).collect(toList());
	}
}
