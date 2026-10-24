CREATE TABLE IF NOT EXISTS tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_categoria_nome_lower
    ON tb_categoria (LOWER(nome));
