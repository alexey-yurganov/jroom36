CREATE TABLE IF NOT EXISTS files (
    id UUID PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT
);
