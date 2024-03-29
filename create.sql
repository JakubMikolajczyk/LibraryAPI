CREATE TYPE roleType AS enum ('USER', 'STAFF', 'ADMIN');

CREATE TABLE users(
    id              serial PRIMARY KEY,
    username        varchar(30) NOT NULL UNIQUE,
    password        varchar(60) NOT NULL,
    email           varchar(30) NOT NULL UNIQUE,
    role            varchar(30) DEFAULT 'USER',
    name            varchar(30) NOT NULL,
    surname         varchar(30) NOT NULL,
    address         varchar(50) NOT NULL,
    city            varchar(30) NOT NULL,
    register_date   timestamp DEFAULT now(),
    delete_date     timestamp DEFAULT null
);

CREATE TABLE author(
    id      SERIAL PRIMARY KEY,
    name    varchar(30) NOT NULL,
    surname varchar(30) NOT NULL
);

CREATE TABLE genre(
    id          serial PRIMARY KEY,
    name        varchar(30) NOT NULL
);

CREATE Table book(
    id          serial PRIMARY KEY,
    ISBN        varchar(20) NOT NULL UNIQUE,
    tittle      varchar(50) NOT NULL,
    year        integer  NOT NULL,
    author_id   integer REFERENCES author(id) NOT NULL,
    delete_date timestamp DEFAULT null
);

CREATE Table book_genre(
    book_id integer REFERENCES book(id),
    genre_id integer REFERENCES genre(id),
    PRIMARY KEY (book_id, genre_id)
);


CREATE TABLE specimen_borrow(
    id          serial PRIMARY KEY,
    book_id     integer REFERENCES book(id) NOT NULL,
    user_id     integer REFERENCES users(id),
    start_time  timestamp DEFAULT null
);

CREATE TABLE borrow_history(
    id         serial PRIMARY KEY,
    book_id    integer REFERENCES book(id) NOT NULL,
    user_id    integer REFERENCES users(id) NOT NULL,
    start_time timestamp NOT NULL,
    end_time   timestamp DEFAULT now() NOT NULL,
    hidden     boolean DEFAULT FALSE
);

CREATE TABLE  token(
    id uuid PRIMARY KEY,
    expire_date timestamp NOT NULL,
    user_id integer REFERENCES  users(id)
);

CREATE FUNCTION remove_expired_token()
RETURNS trigger
AS $$
BEGIN
DELETE FROM token WHERE expire_date < now();
RETURN new;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER token_trigger
    BEFORE INSERT
    ON token
    FOR EACH STATEMENT
    EXECUTE PROCEDURE remove_expired_token();