CREATE TABLE contributionstoclose (
                         contribution_id UUID NOT NULL,
                         member_id UUID NOT NULL,
                         close_date DATE NOT NULL,
                         PRIMARY KEY (contribution_id)
);

