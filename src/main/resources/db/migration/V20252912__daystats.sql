CREATE TABLE day_stats (
                          date DATE NOT NULL,
                          gifts_received INTEGER NOT NULL DEFAULT 0,
                          pending_gift_amount INTEGER NOT NULL DEFAULT 0,
                          PRIMARY KEY (date)
);