CREATE TABLE piggybank (
                           id BIGINT PRIMARY KEY,
                           currentbalance DOUBLE PRECISION NOT NULL,
                           lastmodificationdate TIMESTAMP NOT NULL
);

-- Insert the single row with ID=1
INSERT INTO piggybank (id, currentbalance, lastmodificationdate)
VALUES (1, 0.0, CURRENT_TIMESTAMP);