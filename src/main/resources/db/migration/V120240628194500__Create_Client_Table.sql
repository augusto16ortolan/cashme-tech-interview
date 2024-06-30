DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'client') THEN
        CREATE TABLE client (
            client_id              UUID DEFAULT GEN_RANDOM_UUID() PRIMARY KEY,
            name                   VARCHAR(100) NOT NULL,
            national_identity_type VARCHAR(4)   NOT NULL,
            national_identity      VARCHAR(14)  NOT NULL,
            UNIQUE (national_identity)
        );

        CREATE INDEX client_national_identity_idx ON client (national_identity);
    END IF;
END $$;