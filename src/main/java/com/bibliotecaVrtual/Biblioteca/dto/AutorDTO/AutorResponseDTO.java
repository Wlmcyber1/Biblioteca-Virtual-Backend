package com.bibliotecaVrtual.Biblioteca.dto.AutorDTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutorResponseDTO {

    private Long id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private String biografia;
}
