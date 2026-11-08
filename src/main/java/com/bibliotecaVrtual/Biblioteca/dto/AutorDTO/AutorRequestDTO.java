package com.bibliotecaVrtual.Biblioteca.dto.AutorDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//O QUE CHEGA DA INTERNET
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutorRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @Size(max = 100, message = "A nacionalidade deve ter no máximo 100 caracteres")
    private String nacionalidade;

    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Size(max = 1000, message = "A biografia deve ter no máximo 1000 caracteres")
    private String biografia;
}
