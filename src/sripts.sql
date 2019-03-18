CREATE USER lm_user WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    CREATEDB
    CREATEROLE
    REPLICATION;

CREATE DATABASE lm_database
    WITH
    OWNER = lm_user
    TEMPLATE = template0
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

alter user lm_user WITH PASSWORD 'lm_user';