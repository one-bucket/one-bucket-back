-- default entity id of test Database is 100
SET @id = 100;

INSERT INTO university (id, address, email, name)
VALUES (@id, 'address', 'email@com', 'university');

