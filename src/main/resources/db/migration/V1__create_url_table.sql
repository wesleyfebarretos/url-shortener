CREATE TABLE url (
	id BIGSERIAL PRIMARY KEY,
	short_code VARCHAR(100) NOT NULL,
	original_address TEXT NOT NULL,
	access_qty INT NOT NULL DEFAULT 0,
	expiration_at TIMESTAMPTZ NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ON url(short_code);
CREATE INDEX ON url(expiration_at);