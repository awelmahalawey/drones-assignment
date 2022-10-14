CREATE TABLE IF NOT EXISTS drone (
  id UUID NOT NULL,
   version_no BIGINT,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   is_active BOOLEAN,
   serial_number VARCHAR(100) NOT NULL,
   model VARCHAR(255) NOT NULL,
   state VARCHAR(255) NOT NULL,
   weight_limit DOUBLE NOT NULL,
   battery_cap DOUBLE NOT NULL,
   CONSTRAINT pk_drone PRIMARY KEY (id)
);