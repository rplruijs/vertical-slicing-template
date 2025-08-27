CREATE TABLE members (
                         member_id UUID NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         birth_date DATE NOT NULL,
                         PRIMARY KEY (member_id)
);

-- Add index on email for potential lookups
CREATE INDEX idx_members_email ON members(email);