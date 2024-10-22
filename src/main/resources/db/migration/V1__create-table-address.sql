create table uf(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  acronym CHAR(2) NOT NULL UNIQUE
);

create table cities(
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  uf_id INT NOT NULL,

  FOREIGN KEY (uf_id) REFERENCES uf(id)
);

create table addresses(
  id BIGSERIAL PRIMARY KEY,
  cep char(8) NOT NULL,
  street VARCHAR(100) NOT NULL,
  neighborhood VARCHAR(100) NOT NULL,
  complement VARCHAR(80),
  number INT default 0,
  city_id INT NOT NULL,

  FOREIGN KEY (city_id) REFERENCES cities(id)
);