CREATE TABLE users (
    id uuid primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);
