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

-- Baseline seed for the site-wide page-view counter.
--
-- The original figure lived in Google Analytics (Universal Analytics view
-- 222212743). UA and its APIs were shut down and the data deleted by Google in
-- 2024, and the site was never migrated to GA4, so the real historical total is
-- unrecoverable. This value is an ESTIMATE: current average page views/day from
-- the nginx access logs (the only ~10 days still retained) extrapolated linearly
-- over the site's lifetime since ~2020-10. It very likely overstates the early
-- years; treat it as an order of magnitude, not an exact count.
INSERT INTO page_access (path, hits) VALUES ('/', 7328602);
