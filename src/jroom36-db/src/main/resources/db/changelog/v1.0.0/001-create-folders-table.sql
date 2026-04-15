CREATE TABLE IF NOT EXISTS folders (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    parent_folder_id UUID REFERENCES folders(id)
);

CREATE INDEX idx_folders_name ON folders(name);
