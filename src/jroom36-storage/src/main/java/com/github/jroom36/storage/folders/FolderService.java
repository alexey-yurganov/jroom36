/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders;

import java.util.Collection;
import java.util.UUID;

public interface FolderService {
	Collection<Folder> buildFoldersTree();
	Folder createFolder(String name);
	Folder createFolder(String name, UUID parentFolderId);
}
