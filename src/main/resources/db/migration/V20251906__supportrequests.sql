CREATE TABLE supportrequests (
    request_id UUID PRIMARY KEY,
    amount DECIMAL(19, 2) NOT NULL,
    contribution_id UUID NOT NULL,
    waiting_for_funding BOOLEAN NOT NULL
);