CREATE TABLE if not exists priorities
(
    id       SERIAL PRIMARY KEY,
    name     TEXT UNIQUE NOT NULL,
    position int
);