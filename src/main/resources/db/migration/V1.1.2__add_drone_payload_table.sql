CREATE TABLE IF NOT EXISTS drone_payload (
  id UUID NOT NULL,
   version_no BIGINT,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   is_active BOOLEAN,
   drone_id UUID,
   payload_type VARCHAR(255) NOT NULL,
   payload_identifier VARCHAR(255) NOT NULL,
   state VARCHAR(255) NOT NULL,
   CONSTRAINT pk_drone_payload PRIMARY KEY (id)
);

ALTER TABLE drone_payload DROP CONSTRAINT IF EXISTS FK_DRONE_PAYLOAD_ON_DRONE;
ALTER TABLE drone_payload ADD CONSTRAINT FK_DRONE_PAYLOAD_ON_DRONE FOREIGN KEY (drone_id) REFERENCES drone (id);