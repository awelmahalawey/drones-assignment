INSERT
INTO
  drone
  (id, serial_number, model, state, weight_limit, battery_cap, version_no, created_at, updated_at, is_active)
VALUES
  (RANDOM_UUID(), RANDOM_UUID(), 'LIGHT_WEIGHT', 'IDLE', 100, 100, 0, NOW(), NOW(), TRUE);

INSERT
INTO
  drone
  (id, serial_number, model, state, weight_limit, battery_cap, version_no, created_at, updated_at, is_active)
VALUES
  (RANDOM_UUID(), RANDOM_UUID(), 'CRUISER_WEIGHT', 'IDLE', 300, 30, 0, NOW(), NOW(), TRUE);

INSERT
INTO
  drone
  (id, serial_number, model, state, weight_limit, battery_cap, version_no, created_at, updated_at, is_active)
VALUES
  (RANDOM_UUID(), RANDOM_UUID(), 'MIDDLE_WEIGHT', 'LOADING', 100, 75, 0, NOW(), NOW(), TRUE);

INSERT
INTO
  drone
  (id, serial_number, model, state, weight_limit, battery_cap, last_shipment_started_at, version_no, created_at, updated_at, is_active)
VALUES
  (RANDOM_UUID(), RANDOM_UUID(), 'LIGHT_WEIGHT', 'IDLE', 100, 35, EXTRACT (EPOCH from CURRENT_TIMESTAMP()) * 1000, 0, NOW(), NOW(), TRUE);

INSERT
INTO
  drone
  (id, serial_number, model, state, weight_limit, battery_cap, last_shipment_started_at, version_no, created_at, updated_at, is_active)
VALUES
  (RANDOM_UUID(), RANDOM_UUID(), 'HEAVY_WEIGHT', 'RETURNING', 500, 20, (EXTRACT (EPOCH from CURRENT_TIMESTAMP()) * 1000) - 700000, 0, NOW(), NOW(), TRUE);
