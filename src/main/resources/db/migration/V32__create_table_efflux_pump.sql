CREATE TABLE efflux_pump (
  id BIGINT PRIMARY KEY NOT NULL,
  name VARCHAR(45) NOT NULL
)
WITH (oids = false);

CREATE SEQUENCE seq_efflux_pump;
ALTER TABLE efflux_pump ALTER COLUMN id SET DEFAULT nextval('seq_efflux_pump');

CREATE TABLE efflux_pump_bacteria (
  fk_bacteria BIGINT NOT NULL,
  fk_efflux_pump BIGINT NOT NULL,
  PRIMARY KEY (fk_bacteria, fk_efflux_pump)
)
WITH (oids = false);

ALTER TABLE efflux_pump_bacteria ADD CONSTRAINT fk_efflux_pump_bacteria_bacteria FOREIGN KEY (fk_bacteria) REFERENCES bacteria (id);
ALTER TABLE efflux_pump_bacteria ADD CONSTRAINT fk_efflux_pump_bacteria_efflux_pump FOREIGN KEY (fk_efflux_pump) REFERENCES efflux_pump (id);