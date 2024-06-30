DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'simulation') THEN
        CREATE TABLE simulation (
            simulation_id         UUID                                  DEFAULT gen_random_uuid() PRIMARY KEY,
            simulation_date       TIMESTAMP WITHOUT TIME ZONE  NOT NULL DEFAULT now(),
            requested_value       NUMERIC(9, 2)                NOT NULL DEFAULT 0,
            warranty_value        NUMERIC(9, 2)                NOT NULL DEFAULT 0,
            financing_months      INTEGER                      NOT NULL DEFAULT 1,
            monthly_interest_rate NUMERIC(9, 2)                NOT NULL DEFAULT 0,
            client_id             UUID                         NOT NULL,
            CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE NO ACTION ON UPDATE CASCADE
        );

        CREATE INDEX simulation_client_idx ON simulation (client_id);
    END IF;
END $$;
