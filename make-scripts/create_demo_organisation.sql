CREATE ROLE demo NOINHERIT LOGIN PASSWORD 'password';

GRANT openchs TO demo;

GRANT ALL ON ALL TABLES IN SCHEMA public TO demo;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO demo;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO demo;

INSERT INTO organisation (name, db_user, uuid)
VALUES ('demo', 'demo', '4dafc869-2507-4946-9365-3b82c77fa12d');