package com.bibliotecaVrtual.Biblioteca.dto.LivroDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
}
