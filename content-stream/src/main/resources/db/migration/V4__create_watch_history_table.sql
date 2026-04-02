CREATE TABLE watch_history
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT    NOT NULL REFERENCES users (id),
    content_id      BIGINT    NOT NULL REFERENCES contents (id),
    episode_id      BIGINT    REFERENCES episodes (id),
    watched_seconds INT       NOT NULL DEFAULT 0,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP
);

CREATE INDEX idx_watch_history_user_id ON watch_history (user_id);
CREATE INDEX idx_watch_history_content_id ON watch_history (content_id);
