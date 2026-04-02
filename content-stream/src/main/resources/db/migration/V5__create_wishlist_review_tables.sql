CREATE TABLE wishlists
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL REFERENCES users (id),
    content_id BIGINT    NOT NULL REFERENCES contents (id),
    created_at TIMESTAMP,
    UNIQUE (user_id, content_id)
);

CREATE TABLE reviews
(
    id         BIGSERIAL    PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES users (id),
    content_id BIGINT       NOT NULL REFERENCES contents (id),
    rating     INT          NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP,
    UNIQUE (user_id, content_id)
);

CREATE INDEX idx_wishlists_user_id ON wishlists (user_id);
CREATE INDEX idx_reviews_content_id ON reviews (content_id);
