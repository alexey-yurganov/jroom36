/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files.reposiotry;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FileEntity, UUID> {
}
