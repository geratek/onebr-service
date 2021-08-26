UPDATE bacteria
SET fk_sub_specie = (SELECT s1.id FROM specie s1 WHERE s1."name" = 'Escherichia coli' AND s1.fk_specie_group IS NULL)
WHERE fk_sub_specie = (SELECT s2.id FROM specie s2 WHERE s2."name" = 'Escherichia coli' AND s2.fk_specie_group IS NOT NULL);

UPDATE bacteria
SET fk_sub_specie = (SELECT s1.id FROM specie s1 WHERE s1."name" = 'Klebsiella pneumoniae' AND s1.fk_specie_group IS NULL)
WHERE fk_sub_specie = (SELECT s2.id FROM specie s2 WHERE s2."name" = 'Klebsiella pneumoniae' AND s2.fk_specie_group IS NOT NULL);

DELETE FROM specie WHERE "name" = 'Escherichia coli' AND fk_specie_group IS NOT NULL;
DELETE FROM specie WHERE "name" = 'Klebsiella pneumoniae' AND fk_specie_group IS NOT NULL;

UPDATE specie
SET fk_specie_group = (SELECT s.id FROM specie s WHERE "name" = 'Bactérias-COVID19')
WHERE "name" = 'Escherichia coli' OR "name" = 'Klebsiella pneumoniae' OR "name" = 'Salmonella enterica';
