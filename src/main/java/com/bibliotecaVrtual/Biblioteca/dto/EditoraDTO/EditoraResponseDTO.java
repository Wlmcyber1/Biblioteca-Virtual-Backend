package com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditoraResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String email;
}
