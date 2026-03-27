CREATE TABLE IF NOT EXISTS projects (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  current_pattern_id BIGINT NULL,
  create_time DATETIME NOT NULL,
  update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS file_resources (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  original_name VARCHAR(255) NOT NULL,
  stored_name VARCHAR(255) NOT NULL,
  file_ext VARCHAR(32) NULL,
  content_type VARCHAR(128) NULL,
  file_size BIGINT NOT NULL,
  storage_type VARCHAR(32) NOT NULL,
  relative_path VARCHAR(1024) NOT NULL,
  create_time DATETIME NOT NULL,
  update_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS project_patterns (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  source_type VARCHAR(32) NOT NULL,
  display_name VARCHAR(255) NOT NULL,
  file_id BIGINT NULL,
  external_url VARCHAR(1024) NULL,
  create_time DATETIME NOT NULL,
  update_time DATETIME NOT NULL,
  INDEX idx_project_patterns_project_id (project_id)
);

CREATE TABLE IF NOT EXISTS pattern_configs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  pattern_id BIGINT NOT NULL,
  current_page INT NOT NULL,
  mask_top_offset INT NOT NULL,
  mask_height INT NOT NULL,
  update_time DATETIME NOT NULL,
  UNIQUE KEY uk_pattern_configs_project_pattern (project_id, pattern_id)
);

CREATE TABLE IF NOT EXISTS project_progress (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL,
  current_row_index INT NOT NULL,
  total_count INT NOT NULL,
  total_seconds BIGINT NOT NULL,
  update_time DATETIME NOT NULL,
  UNIQUE KEY uk_project_progress_project_id (project_id)
);
