CREATE TABLE supportsapproved (
    request_id UUID PRIMARY KEY,
    contribution_id UUID NOT NULL,
    requested_by UUID NOT NULL,
    requested_for UUID NOT NULL,
    month VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION,
    status VARCHAR(255) NOT NULL
);