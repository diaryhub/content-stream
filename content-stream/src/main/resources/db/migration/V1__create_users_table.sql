CREATE TABLE users
(
    id          BIGSERIAL    PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255),
    nickname    VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    provider    VARCHAR(50)  NOT NULL,
    provider_id VARCHAR(255),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);
