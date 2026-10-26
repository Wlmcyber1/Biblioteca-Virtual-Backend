package com.bibliotecaVrtual.Biblioteca.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_livro")
public class LivroModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false) //equivale a not null
    private String titulo;

    @Column (nullable = false)
    private String autor;

    @ManyToOne(optional = false) //muitos livros pertencem a uma categoria
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaModel categoria;

    private String descricao;
    private Integer anoPublicacao;
    private String urlArquivo;
}
