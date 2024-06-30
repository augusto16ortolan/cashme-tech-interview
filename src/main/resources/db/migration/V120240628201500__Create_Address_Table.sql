DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_tables WHERE tablename = 'address') THEN
        CREATE TABLE address (
            address_id   UUID DEFAULT GEN_RANDOM_UUID() PRIMARY KEY,
            address_type VARCHAR(15)   NOT NULL,
            address      VARCHAR(100)  NOT NULL,
            number       VARCHAR(20)   NOT NULL,
            neighborhood VARCHAR(100)  NOT NULL,
            zip_code     VARCHAR(20)   NOT NULL,
            city         VARCHAR(100)  NOT NULL,
            uf           VARCHAR(2)    NOT NULL,
            client_id    UUID          NOT NULL,
            CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES client (client_id) ON DELETE CASCADE
        );

        CREATE INDEX address_client_idx ON address (client_id);
    END IF;
END $$;
