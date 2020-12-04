CREATE TABLE scc_mec_element (
  id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(45) NOT NULL
)
WITH (oids = false);

CREATE SEQUENCE seq_scc_mec_element;
ALTER TABLE scc_mec_element ALTER COLUMN id SET DEFAULT nextval('seq_scc_mec_element');

ALTER TABLE bacteria ADD COLUMN fk_scc_mec_element BIGINT;
ALTER TABLE bacteria ADD CONSTRAINT fk_bacteria_scc_mec_element FOREIGN KEY (fk_scc_mec_element) REFERENCES scc_mec_element (id);