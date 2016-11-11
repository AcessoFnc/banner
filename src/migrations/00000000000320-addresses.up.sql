CREATE TABLE addresses					-- #1
( addr_id bigint NOT NULL,
  public_place character varying(255) NOT NULL,
  addr_number integer NOT NULL,
  zip_code character(8) NOT NULL, -- V.:...
  addr_compl character varying(255),
  district character varying(255),
  city character varying(255) NOT NULL,
  state character varying(255) NOT NULL,
  country character varying(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  updated_at TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  CONSTRAINT addr_id_pk PRIMARY KEY (addr_id),
  CONSTRAINT zip_numb_place_unique UNIQUE (zip_code, public_place, addr_number));
--;;
-- create an update trigger which updates our update_date -- column by calling the above function
CREATE TRIGGER update_address_updated_at BEFORE UPDATE --#2
ON clients FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
--;;
INSERT INTO addresses (public_place, addr_number, zip_code, city, state, country)
        VALUES ('Rua 1', 10, '01000100', 'Cidade 1', 'Estado 1', 'Pais 1');
--;;
INSERT INTO addresses (public_place, addr_number, zip_code, city, state, country)
        VALUES ('Av. 2', 20, '02000200', 'Cidade 2', 'Estado 2', 'Pais 2');
--;;
INSERT INTO addresses (public_place, addr_number, zip_code, city, state, country)
        VALUES ('Pça. 3', 30, '03000300', 'Cidade 3', 'Estado 3', 'Pais 3');
--;;
INSERT INTO addresses (public_place, addr_number, zip_code, city, state, country)
        VALUES ('Rua 4', 40, '04000400', 'Cidade 4', 'Estado 4', 'Pais 4');
--;;
INSERT INTO addresses (public_place, addr_number, zip_code, city, state, country)
        VALUES ('Al. 5', 50, '05000500', 'Cidade 5', 'Estado 5', 'Pais 5');
