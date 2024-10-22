create table transactions(
  id BIGSERIAL PRIMARY KEY,
  amount DECIMAL(12, 2) NOT NULL,
  payer_id BIGINT NOT NULL,
  receive_id BIGINT NOT NULL,
  created_at TIMESTAMP default CURRENT_TIMESTAMP,

  FOREIGN KEY (payer_id) REFERENCES users(id),
  FOREIGN KEY (receive_id) REFERENCES users(id)
);