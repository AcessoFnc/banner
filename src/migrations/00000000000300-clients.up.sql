CREATE TABLE clients					-- #1
( client_id BIGINT SERIAL NOT NULL PRIMARY KEY,
  client_name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  updated_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  cpf character varying(11) NOT NULL,
  addr_id bigint,
  birth_date date,
  mother_name character varying(255),
  ca_id bigint,
  CONSTRAINT client_uniq UNIQUE (client_name, cpf, birth_date, mother_name));
--;;
-- create an update trigger which updates our update_date -- column by calling the above function
CREATE TRIGGER update_client_updated_at BEFORE UPDATE --#2
ON clients FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
--;;
INSERT INTO clients (client_name) VALUES ('Jose Carlos');
--;;
INSERT INTO clients (client_name) VALUES ('Joao da Silva');
--;;
INSERT INTO clients (client_name) VALUES ('Maria da Silva');
--;;
INSERT INTO clients (client_name) VALUES ('Maria Joana Pereira');
--;;
INSERT INTO clients (client_name) VALUES ('Joao Silveira');
