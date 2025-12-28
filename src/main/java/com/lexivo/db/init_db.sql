ROLLBACK;
BEGIN;

CREATE DOMAIN ID as VARCHAR(200);
CREATE DOMAIN SHORT_TEXT as VARCHAR(255);

CREATE TABLE IF NOT EXISTS users(
    email SHORT_TEXT PRIMARY KEY,
    name SHORT_TEXT NOT NULL,
    password_hash SHORT_TEXT NOT NULL,
    confirmed BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS email_confirmation_codes(
    email SHORT_TEXT PRIMARY KEY,
    code CHAR(7) NOT NULL,
    created_at BIGINT NOT NULL,
    expires_at BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS logs(
    created_at BIGINT NOT NULL,
    category SHORT_TEXT NOT NULL,
    stack_trace TEXT[],
    messages TEXT[] NOT NULL,
    user_email SHORT_TEXT REFERENCES users(email)
);

COMMIT;