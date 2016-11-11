-- name: get-recently-added
-- Gets the 10 most recently added fin moves in the db.
SELECT cli.name as client, fm.fin_move_id, fm.fin_release as fin_move_name, fm.release_date, fm.created_at
FROM clients cli
INNER JOIN fin_moves fm ON cli.client_id = fm.client_id
ORDER BY fm.created_at DESC
LIMIT 10;

-- name: get-by-client
-- Gets the fin moves for a given client.
-- Expects :client, the client name.
SELECT fm.fin_move_id, fm.fin_release, fm.release_date, fm.created_at
FROM fin_moves fm
INNER JOIN clients cli ON fm.client_id = cli.client_id
WHERE cli.name = :client
ORDER BY fm.release_date DESC;

-- name: insert-fin_move<!
-- Adds the fin move for the given client to the database
-- EXPECTS :client_id, :fin_move_name, and :release_date
INSERT INTO fin_moves (client_id, fin_release, release_date)
VALUES (:client_id, :fin_move_name, date(:release_date));

-- name: get-fin_moves-by-name
-- Fetches the specific fin_move from the database for a particular client.
-- Expects :client_id and :fin_move_name.
SELECT *
FROM fin_moves
WHERE
  client_id = :client_id and
  fin_release = :fin_move_name;

-- name: ins-fin-move-by-username<!
-- Adds the specific fin_move for a particular client to the database.
INSERT INTO fin_moves (client_id, ca_id, fin_release, release_date)
SELECT c.CLIENT_ID, c.ca_id, :fin_release, date(:release_date)
  	from clients c
  		inner join users u
  		on c.client_id = u.client_id
  	where u.username = 'banner';
