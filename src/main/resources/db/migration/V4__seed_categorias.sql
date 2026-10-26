INSERT INTO tb_categoria (nome) VALUES
    ('Matemática'),
    ('Língua Portuguesa'),
    ('Literatura'),
    ('Redação'),
    ('Inglês'),
    ('História'),
    ('Geografia'),
    ('Filosofia'),
    ('Sociologia'),
    ('Física'),
    ('Química'),
    ('Biologia'),
    ('Artes'),
    ('Educação Física'),
    ('ENEM e Vestibulares'),
    ('Paradidáticos'),
    ('Material de apoio')
ON CONFLICT DO NOTHING;
