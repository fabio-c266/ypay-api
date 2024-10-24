create table users(
  id BIGSERIAL PRIMARY KEY,
  full_name VARCHAR(100) NOT NULL,
  user_type VARCHAR(30) NOT NULL,
  balance DECIMAL(12, 2) NOT NULL,
  email VARCHAR(80) NOT NULL UNIQUE,
  phone CHAR(11) NOT NULL UNIQUE,
  cpf CHAR(11) UNIQUE,
  cnpj VARCHAR(19) UNIQUE,
  active BOOLEAN NOT NULL DEFAULT true,
  address_id BIGINT NOT NULL UNIQUE,
  created_at TIMESTAMP default CURRENT_TIMESTAMP,

  FOREIGN KEY (address_id) REFERENCES addresses(id)
);