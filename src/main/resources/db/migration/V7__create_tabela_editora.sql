CREATE TABLE tb_editora (
    id       BIGSERIAL    PRIMARY KEY,
    nome     VARCHAR(150) NOT NULL,
    cnpj     VARCHAR(18),
    email    VARCHAR(150)
);
