CREATE TABLE user_credentials (
    member_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_created TIMESTAMP NOT NULL,
    PRIMARY KEY (member_id)
);

-- Add index on email for efficient lookups
CREATE UNIQUE INDEX idx_user_credentials_email ON user_credentials(email);
