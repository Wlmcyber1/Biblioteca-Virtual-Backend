package com.bibliotecaVrtual.Biblioteca.dto.LivroDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//O QUE CHEGA DA INTERNET
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivroRequestDTO{

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "O autor é obrigatório")
    private String autor;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    private Integer anoPublicacao;

    @Size(max = 500, message = "A URL do arquivo deve ter no máximo 500 caracteres")
    private String urlArquivo;
}
