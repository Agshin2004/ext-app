-- liquibase formatted sql


--changeset agshin2004:010-roles (labels=roles)
-- CREATE TABLE roles (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(25)
-- );
--rollback DROP TABLE roles;

--changeset agshin2004:010-roles-data (labels=roles)
-- CREATE TABLE user_roles
-- (
--     id      SERIAL PRIMARY KEY,
--     user_id INTEGER REFERENCES users (id),
--     role_id INTEGER references roles (id)
-- );
--rollback DELETE FROM roles WHERE name = 'ADMIN';
