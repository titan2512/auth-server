CREATE USER lm_auth WITH
    LOGIN
    NOSUPERUSER
    INHERIT
    CREATEDB
    CREATEROLE
    REPLICATION;

CREATE DATABASE lm_planning_auth
    WITH
    OWNER = lm_auth
    TEMPLATE = template0
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

