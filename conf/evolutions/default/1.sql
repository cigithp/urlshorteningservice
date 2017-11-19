# Tasks schema

# --- !Ups

CREATE SEQUENCE url_id_seq;
CREATE TABLE url (
    id INTEGER NOT NULL DEFAULT nextval('url_id_seq'),
    longurl VARCHAR(255),
    PRIMARY KEY(id)
    );

# --- !Downs
DROP SEQUENCE url_id_seq;
DROP TABLE url;