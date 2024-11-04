CREATE TABLE users
(
    id       INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name     VARCHAR(100),
    email    VARCHAR(100),
    password VARCHAR(100),
    role     VARCHAR(100),
    salt     VARCHAR(100)
);
