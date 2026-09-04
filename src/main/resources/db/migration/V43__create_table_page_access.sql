CREATE TABLE page_access (
  id BIGINT PRIMARY KEY NOT NULL,
  path VARCHAR(500) NOT NULL,
  hits BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT now(),
  updated_at TIMESTAMP NOT NULL DEFAULT now()
)
WITH (oids = false);

CREATE SEQUENCE seq_page_access;
ALTER TABLE page_access ALTER COLUMN id SET DEFAULT nextval('seq_page_access');

CREATE UNIQUE INDEX page_access_path_idx ON page_access ("path");

-- Baseline seed: historical page views migrated from Google Analytics (Universal
-- Analytics view 222212743, range 2019-01-01..2024-06-30, when UA stopped serving
-- data). Add one row per tracked path with the last known GA "ga:pageviews" total
-- so the counter continues from the real number instead of restarting at zero.
--
-- INSERT INTO page_access (path, hits) VALUES ('/', 123456);
