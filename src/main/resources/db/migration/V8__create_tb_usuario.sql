CREATE TABLE tb_usuario (
    id             BIGSERIAL    PRIMARY KEY,
    nome           VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    matricula      VARCHAR(20)  NOT NULL UNIQUE,
    perfil         VARCHAR(20)  NOT NULL,
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE,
    data_cadastro  TIMESTAMP    NOT NULL DEFAULT NOW()
);
