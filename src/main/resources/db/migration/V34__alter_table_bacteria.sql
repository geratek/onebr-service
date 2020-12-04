ALTER TABLE bacteria ADD COLUMN fk_sub_specie BIGINT;
ALTER TABLE bacteria ADD CONSTRAINT fk_bacteria_sub_specie_specie FOREIGN KEY (fk_sub_specie) REFERENCES specie (id);