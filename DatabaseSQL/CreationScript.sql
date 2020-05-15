CREATE TABLE users (
    id serial primary key,
    name varchar(120) not null,
    email varchar(30) unique not null,
    password varchar(60) not null,
    user_type varchar(12) not null
);

CREATE TABLE productions (
    id serial primary key,
    name varchar(120) not null,
    release_date date not null,
    status varchar(8) not null,
    tv_code char(4),
    associated_producer integer references users(id)
);

CREATE TABLE cast_members (
    id serial primary key,
    first_name varchar(60) not null,
    last_name varchar(30) not null,
    email varchar(30) unique not null,
    bio varchar(500)
);


CREATE TABLE production_to_cast (
    id serial primary key,
    production_id integer references productions(id),
    cast_id integer references cast_members(id)
);

CREATE TABLE roles (
    id serial primary key,
    role_name varchar(60) not null,
    production_id integer references productions(id)
);

CREATE TABLE cast_to_roles (
    id serial primary key,
    cast_id integer references cast_members(id),
    role_id integer references roles(id)
);
