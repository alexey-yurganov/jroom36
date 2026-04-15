CREATE TABLE IF NOT EXISTS files (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    content OID,
    size_bytes BIGINT
);

CREATE INDEX idx_files_name ON files(name);
