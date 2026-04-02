CREATE TABLE contents
(
    id                BIGSERIAL    PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    description       TEXT,
    thumbnail_url     VARCHAR(500),
    content_type      VARCHAR(20)  NOT NULL,
    genre             VARCHAR(50)  NOT NULL,
    release_year      INT          NOT NULL,
    minimum_plan_type VARCHAR(20)  NOT NULL,
    created_at        TIMESTAMP
);

CREATE TABLE episodes
(
    id               BIGSERIAL    PRIMARY KEY,
    content_id       BIGINT       NOT NULL REFERENCES contents (id),
    episode_number   INT          NOT NULL,
    title            VARCHAR(255) NOT NULL,
    duration_minutes INT          NOT NULL DEFAULT 0,
    created_at       TIMESTAMP
);

-- 샘플 데이터
INSERT INTO contents (title, description, thumbnail_url, content_type, genre, release_year, minimum_plan_type)
VALUES ('액션 히어로', '평범한 시민이 슈퍼히어로로 거듭나는 이야기', null, 'MOVIE', 'ACTION', 2024, 'FREE'),
       ('로맨스 인 서울', '서울에서 펼쳐지는 달달한 로맨스', null, 'MOVIE', 'ROMANCE', 2023, 'BASIC'),
       ('미스터리 하우스', '버려진 저택에서 벌어지는 공포 시리즈', null, 'SERIES', 'HORROR', 2024, 'PREMIUM'),
       ('우주 탐험대', '인류 최초의 우주 탐험을 담은 SF 대작', null, 'MOVIE', 'SF', 2024, 'BASIC'),
       ('코미디 패밀리', '좌충우돌 가족의 일상 코미디 시리즈', null, 'SERIES', 'COMEDY', 2023, 'FREE');

-- 시리즈 에피소드
INSERT INTO episodes (content_id, episode_number, title, duration_minutes)
VALUES (3, 1, '첫 번째 밤', 45),
       (3, 2, '사라진 그림', 42),
       (3, 3, '비밀의 방', 50),
       (5, 1, '첫 만남', 30),
       (5, 2, '가족 여행', 28),
       (5, 3, '소동 대작전', 32);
