/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files;

import java.util.UUID;

public record FileInfo(UUID id, String name, Long sizeBytes, UUID folderId) {}
