package com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurmaResponseDTO {

    private Long id;
    private Integer serie;
    private String letra;
    private Integer anoLetivo;
    private Long professorId;
    private String professorNome;
    private List<LivroResumoDTO> livros;
}
