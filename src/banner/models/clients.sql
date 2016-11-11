-- name: insert-client<!
-- Inserts a new client into the database.
-- Expects :name.
INSERT INTO clients(name)
VALUES (:client_name);

-- name: get-clients-by-name
-- Retrieves an client from the database by name.
-- Expects :client_name
SELECT *
FROM clients
WHERE name=:client_name;

-- name: get-client-by-username
-- Retrieves an client from teh user id
-- Expects :username
SELECT name as client_name, cpf, to_char(birth_date, 'YYYYMMDD') as birth_date
FROM CLIENTS C
INNER JOIN USERS U
ON C.client_id = U.client_id
WHERE U.username = :username;

-- name: get-client-id-1
-- Retrieves an client from teh user id
SELECT name as client_name, cpf, to_char(birth_date, 'YYYYMMDD') as birth_date
FROM CLIENTS C
WHERE C.client_id = 1;

-- name: get-client-ca-by-username
-- Retrieves client and curr. acc. from the user id
-- Expects :username
SELECT name as client_name, cpf, to_char(birth_date, 'dd/mm/yyyy') as birth_date,
       C.ca_id as ca_id, CA.fb as fb
FROM CLIENTS C
INNER JOIN USERS U
ON C.client_id = U.client_id
INNER JOIN ca
ON C.ca_id = CA.ca_id
WHERE U.username = :username;

-- name: get-fin-moves-by-username
-- Retrieves client and curr. acc. from the user id
-- Expects :username
SELECT fm.ca_id, fm.fin_release, to_char(fm.release_date, 'dd/mm/yyyy') as release_date, name as client_name
FROM fin_moves fm
INNER JOIN clients c
ON C.ca_id = fm.ca_id
INNER JOIN users u
ON fm.client_id = u.client_id
WHERE U.username = :username;
