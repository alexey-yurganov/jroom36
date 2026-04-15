/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.storage.files.reposiotry;

import java.sql.Blob;
import java.util.UUID;

import com.github.jroom36.storage.files.FileInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "files")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Accessors(chain = true)
public class FileEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@Column
	private String name;

	@Lob
	@Column(columnDefinition = "OID")
	private Blob content;
	private Long sizeBytes;

	public FileInfo toRecord() {
		return new FileInfo(id, name, sizeBytes);
	}
}
