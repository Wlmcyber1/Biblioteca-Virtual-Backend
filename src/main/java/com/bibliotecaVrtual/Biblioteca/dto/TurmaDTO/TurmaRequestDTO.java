package com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//O QUE CHEGA DA INTERNET
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurmaRequestDTO {

    @NotNull(message = "A série é obrigatória")
    @Min(value = 1, message = "A série deve ser entre 1 e 3")
    @Max(value = 3, message = "A série deve ser entre 1 e 3")
    private Integer serie;

    @NotBlank(message = "A letra é obrigatória")
    @Pattern(regexp = "[A-Za-z]", message = "A letra deve ser uma única letra")
    private String letra;

    @NotNull(message = "O ano letivo é obrigatório")
    @Min(value = 2000, message = "O ano letivo deve ser a partir de 2000")
    private Integer anoLetivo;

    @NotNull(message = "O professor é obrigatório")
    private Long professorId;

    //ids dos livros recomendados; pode ser uma lista vazia
    @NotNull(message = "A lista de livros é obrigatória (pode ser vazia)")
    private List<Long> livroIds;
}
