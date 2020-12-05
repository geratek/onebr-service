ALTER TABLE specie ADD COLUMN fk_specie_group BIGINT;
ALTER TABLE specie ADD CONSTRAINT fk_specie_specie FOREIGN KEY (fk_specie_group) REFERENCES specie (id);

DROP INDEX specie_name_idx;
CREATE UNIQUE INDEX specie_name_fk_specie_group_idx ON specie ("name", fk_specie_group);

INSERT INTO specie(name, fk_specie_group) VALUES('Escherichia coli', (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19'));
INSERT INTO specie(name, fk_specie_group) VALUES('Klebsiella pneumoniae', (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19'));

UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Acinetobacter baumannii';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Citrobacter freundii';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Enterococcus faecalis';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Klebsiella aerogenes';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Pseudomonas aeruginosa';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Staphylococcus aureus';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Staphylococcus epidermidis';
UPDATE specie SET fk_specie_group = (SELECT s1.id FROM specie s1 WHERE s1.name = 'Bactérias-COVID19') WHERE name = 'Staphylococcus lugdunensis';
