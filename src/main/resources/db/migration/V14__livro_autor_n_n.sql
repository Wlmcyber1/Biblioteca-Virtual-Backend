-- N:N: um livro pode ter vários autores, e um autor pode ter vários livros
CREATE TABLE tb_livro_autor (
    livro_id BIGINT NOT NULL REFERENCES tb_livro (id) ON DELETE CASCADE,
    autor_id BIGINT NOT NULL REFERENCES tb_autor (id),
    PRIMARY KEY (livro_id, autor_id)
);

-- Converte o autor em texto dos livros já cadastrados, sem perder dados:

-- 1) ajusta o texto ao tamanho do nome na tb_autor (150) e tira espaços das pontas
UPDATE tb_livro SET autor = LEFT(TRIM(autor), 150);

-- 2) cria na tb_autor os nomes que ainda não existem (sem diferenciar maiúsculas de minúsculas)
INSERT INTO tb_autor (nome)
SELECT MIN(l.autor)
FROM tb_livro l
WHERE NOT EXISTS (SELECT 1 FROM tb_autor a WHERE LOWER(a.nome) = LOWER(l.autor))
GROUP BY LOWER(l.autor);

-- 3) liga cada livro ao autor de mesmo nome (se o nome estiver repetido na tb_autor, usa o de menor id)
INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, (SELECT MIN(a.id) FROM tb_autor a WHERE LOWER(a.nome) = LOWER(l.autor))
FROM tb_livro l;

-- 4) o autor em texto deixa de existir; agora o autor vem da tb_autor
ALTER TABLE tb_livro DROP COLUMN autor;
