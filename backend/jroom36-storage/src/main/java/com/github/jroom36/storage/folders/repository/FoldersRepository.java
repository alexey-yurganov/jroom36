/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoldersRepository extends JpaRepository<FolderEntity, UUID> {
}
