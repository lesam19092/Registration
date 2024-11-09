CREATE TABLE users
(
    id       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name     VARCHAR(100),
    email    VARCHAR(100),
    password VARCHAR(100),
    role     VARCHAR(100)
);

create table tokens
(
    id            INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    access_token  VARCHAR(200),
    is_logged_out boolean,
    user_id       INT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
