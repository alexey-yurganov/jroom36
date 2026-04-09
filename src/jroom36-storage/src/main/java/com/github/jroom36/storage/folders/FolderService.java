/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders;

import java.util.Collection;
import java.util.UUID;

public interface FolderService {
	Collection<Folder> getFolders();
	void createFolder(String name);
	void createFolder(String name, UUID parentFolderId);
}
