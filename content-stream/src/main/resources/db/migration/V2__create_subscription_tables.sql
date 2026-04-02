CREATE TABLE plans
(
    id          BIGSERIAL    PRIMARY KEY,
    plan_type   VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    max_devices INT          NOT NULL
);

CREATE TABLE subscriptions
(
    id          BIGSERIAL    PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES users (id),
    plan_id     BIGINT       NOT NULL REFERENCES plans (id),
    status      VARCHAR(50)  NOT NULL,
    started_at  TIMESTAMP,
    expired_at  TIMESTAMP
);

-- 플랜 초기 데이터
INSERT INTO plans (plan_type, description, max_devices)
VALUES ('FREE',    '광고 포함, SD 화질, 1개 기기', 1),
       ('BASIC',   '광고 없음, FHD 화질, 2개 기기', 2),
       ('PREMIUM', '광고 없음, 4K 화질, 4개 기기', 4);
