package com.bibliotecaVrtual.Biblioteca.dto.LivroDTO;

import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorResponseDTO;
import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private List<AutorResponseDTO> autores;
    private CategoriaResponseDTO categoria;
    private EditoraResponseDTO editora;
    private String descricao;
    private Integer anoPublicacao;
    private String urlArquivo;
}
