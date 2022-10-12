CREATE TABLE IF NOT EXISTS medication (
  id UUID NOT NULL,
   version_no BIGINT,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   is_active BOOLEAN,
   name VARCHAR(255) NOT NULL,
   code VARCHAR(255) NOT NULL,
   weight DOUBLE NOT NULL,
   image_url VARCHAR(255),
   CONSTRAINT pk_medication PRIMARY KEY (id)
);

ALTER TABLE medication DROP CONSTRAINT IF EXISTS uc_bb22c232fb6ef75795303deb2;
ALTER TABLE medication ADD CONSTRAINT uc_bb22c232fb6ef75795303deb2 UNIQUE (name, code);