package com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO;

import lombok.*;

//versão enxuta do livro, usada dentro da resposta da turma
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroResumoDTO {

    private Long id;
    private String titulo;
}
