package com.bibliotecaVrtual.Biblioteca.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany //um livro pode ter vários autores, e um autor pode ter vários livros
    @JoinTable(
            name = "tb_livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<AutorModel> autores = new ArrayList<>();

    @ManyToOne(optional = false) //muitos livros pertencem a uma categoria
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaModel categoria;

    @ManyToOne //muitos livros podem ser da mesma editora; opcional
    @JoinColumn(name = "editora_id")
    private EditoraModel editora;

    private String descricao;
    private Integer anoPublicacao;
    private String urlArquivo;
}
