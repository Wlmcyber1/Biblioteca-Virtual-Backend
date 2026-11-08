-- Alinha o banco com LivroModel (categoria obrigatória).
-- V3 criou categoria_id como nullable; não editamos V3 porque ela já pode ter sido aplicada.
-- Livros antigos sem categoria recebem a categoria "Material de apoio" (criada na V4).
-- Se não existir essa categoria e houver livros sem categoria, o SET NOT NULL falha (falha segura).
UPDATE tb_livro
SET categoria_id = (SELECT id FROM tb_categoria WHERE nome = 'Material de apoio')
WHERE categoria_id IS NULL;

ALTER TABLE tb_livro
    ALTER COLUMN categoria_id SET NOT NULL;
