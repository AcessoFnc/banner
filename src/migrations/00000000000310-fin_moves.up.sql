CREATE TABLE fin_moves
(fin_move_id  SERIAL      NOT NULL PRIMARY KEY,
 client_id    BIGINT NOT NULL REFERENCES clients (client_id),
 fin_release  VARCHAR(50),
 release_date DATE NOT NULL,
 created_at   TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 updated_at   TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 CONSTRAINT client_move UNIQUE (client_id, fin_release));
--;;
-- create an update trigger which updates our update_date column by calling the above function
CREATE TRIGGER update_fin_move_update_date BEFORE UPDATE
ON fin_moves FOR EACH ROW EXECUTE PROCEDURE
update_updated_at();
--;;
INSERT INTO fin_moves (client_id, fin_release, release_date)
  SELECT c.client_id, '10.21', '1978-11-24'
  FROM clients c
  WHERE c.client_name = 'Jose Carlos'
--;;
INSERT INTO fin_moves (client_id, fin_release, release_date)
  SELECT c.client_id, '11.22', '2000-04-18'
  FROM clients c
  WHERE c.client_name = 'Joao da Silva'
--;;
INSERT INTO fin_moves (client_id, fin_release, release_date)
  SELECT c.client_id, '12.23', '1992-11-29'
  FROM clients c
  WHERE c.client_name = 'Maria da Silva'
