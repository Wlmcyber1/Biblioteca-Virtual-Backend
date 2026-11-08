-- Bloqueia a exclusão de editora vinculada a livro (igual a Categoria e Autor).
-- A V10 criou a FK com ON DELETE SET NULL; não editamos a V10, porque ela já está na developer.
-- O nome da constraint é gerado pelo Postgres, então ela é encontrada pela coluna, não pelo nome.
DO $$
DECLARE
    r record;
BEGIN
    FOR r IN
        SELECT con.conname
        FROM pg_constraint con
        JOIN pg_attribute att ON att.attrelid = con.conrelid AND att.attnum = ANY (con.conkey)
        WHERE con.conrelid = 'tb_livro'::regclass
          AND con.contype = 'f'
          AND att.attname = 'editora_id'
    LOOP
        EXECUTE format('ALTER TABLE tb_livro DROP CONSTRAINT %I', r.conname);
    END LOOP;
END $$;

ALTER TABLE tb_livro
    ADD CONSTRAINT fk_livro_editora FOREIGN KEY (editora_id) REFERENCES tb_editora (id);