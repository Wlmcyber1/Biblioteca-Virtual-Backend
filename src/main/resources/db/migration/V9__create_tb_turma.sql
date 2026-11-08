CREATE TABLE tb_turma (
    id           BIGSERIAL   PRIMARY KEY,
    serie        INTEGER     NOT NULL CHECK (serie BETWEEN 1 AND 3),
    letra        VARCHAR(1)  NOT NULL,
    ano_letivo   INTEGER     NOT NULL,
    professor_id BIGINT      NOT NULL REFERENCES tb_usuario (id),
    CONSTRAINT uk_turma_serie_letra_ano UNIQUE (serie, letra, ano_letivo)
);

-- N:N: uma turma pode ter vários livros recomendados, e um livro pode estar em várias turmas
CREATE TABLE tb_turma_livro (
    turma_id BIGINT NOT NULL REFERENCES tb_turma (id) ON DELETE CASCADE,
    livro_id BIGINT NOT NULL REFERENCES tb_livro (id),
    PRIMARY KEY (turma_id, livro_id)
);
