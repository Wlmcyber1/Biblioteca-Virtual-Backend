package com.bibliotecaVrtual.Biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_turma")
public class TurmaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer serie;

    @Column(nullable = false, length = 1)
    private String letra;

    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    //cada turma tem um professor (usuário com perfil PROFESSOR)
    @ManyToOne(optional = false)
    @JoinColumn(name = "professor_id", nullable = false)
    private UsuarioModel professor;

    //livros recomendados pelo professor para a turma (N:N, quantos ele quiser)
    @ManyToMany
    @JoinTable(
            name = "tb_turma_livro",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private Set<LivroModel> livros = new HashSet<>();
}
