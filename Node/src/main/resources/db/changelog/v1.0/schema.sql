DROP TABLE IF EXISTS bot_users;

CREATE TABLE IF NOT EXISTS bot_users (
    chat_id VARCHAR PRIMARY KEY,
    state VARCHAR,
    companion_id VARCHAR,
    FOREIGN KEY (companion_id) REFERENCES bot_users (chat_id)
);