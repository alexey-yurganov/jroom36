/* (c) 2026 alexey-yurganov, MIT License */
package com.github.jroom36.storage.folders;

import java.util.Collection;
import java.util.UUID;

public record Folder(UUID id, String name, UUID parentFolderId, Collection<Folder> nestedFolders) {}
