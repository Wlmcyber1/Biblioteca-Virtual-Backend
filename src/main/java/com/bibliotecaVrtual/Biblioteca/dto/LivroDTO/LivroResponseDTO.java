package com.bibliotecaVrtual.Biblioteca.dto.LivroDTO;

import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private CategoriaResponseDTO categoria;
    private String descricao;
    private Integer anoPublicacao;
    private String urlArquivo;
}
