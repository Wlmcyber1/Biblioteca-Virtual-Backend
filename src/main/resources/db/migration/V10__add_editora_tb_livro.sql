-- liga o livro à editora; opcional, para não invalidar os livros já cadastrados
ALTER TABLE tb_livro
    ADD COLUMN editora_id BIGINT REFERENCES tb_editora (id) ON DELETE SET NULL;
