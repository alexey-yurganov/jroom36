/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.folders.repository;

import java.util.UUID;

import com.github.jroom36.storage.folders.Folder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static java.util.Collections.emptyList;

@Entity
@Table(name = "folders")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Accessors(chain = true)
public class FolderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column
	private String name;
	@Column
	private UUID parentFolderId;

	public boolean hasParentFolder() {
		return parentFolderId != null;
	}
	public boolean isTopLevelFolder() {
		return !hasParentFolder();
	}
	public Folder toRecord() {
		return new Folder(id, name, parentFolderId, emptyList());
	}
}
