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

CREATE TABLE category(
    id          serial PRIMARY KEY,
    name        varchar(30) NOT NULL,
    parent_id   integer REFERENCES category(id)
);

CREATE Table book(
    id          serial PRIMARY KEY,
    ISBN        varchar(20) NOT NULL UNIQUE,
    tittle      varchar(50) NOT NULL,
    year        integer  NOT NULL,
    author_id   integer REFERENCES author(id),
    category_id integer REFERENCES category(id),
    delete_date timestamp DEFAULT null
);

CREATE TABLE specimen(
    id       serial PRIMARY KEY,
    book_id  integer REFERENCES book(id)
);

CREATE TABLE borrow(
    id          serial PRIMARY KEY ,
    specimen_id integer REFERENCES specimen(id),
    user_id     integer REFERENCES users(id),
    start_time  timestamp DEFAULT now()
);

CREATE TABLE borrow_history(
    id         serial PRIMARY KEY,
    book_id    integer REFERENCES book(id),
    user_id    integer REFERENCES users(id),
    start_time timestamp NOT NULL,
    end_time   timestamp DEFAULT now()
);

CREATE TABLE  token(
    id uuid PRIMARY KEY,
    expire_date timestamp NOT NULL,
    user_id integer REFERENCES  users(id)
);