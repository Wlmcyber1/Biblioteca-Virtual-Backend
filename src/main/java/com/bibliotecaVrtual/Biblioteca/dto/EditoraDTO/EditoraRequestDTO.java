package com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class EditoraRequestDTO {

    //aceita 00.000.000/0000-00 ou 00000000000000
    public static final String CNPJ_REGEX = "^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$";
    public static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @Pattern(regexp = CNPJ_REGEX, message = "CNPJ inválido")
    private String cnpj;

    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    @Pattern(regexp = EMAIL_REGEX, message = "E-mail inválido")
    private String email;
}
