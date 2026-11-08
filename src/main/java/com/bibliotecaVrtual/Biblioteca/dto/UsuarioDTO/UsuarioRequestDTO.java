package com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO;

import com.bibliotecaVrtual.Biblioteca.model.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UsuarioRequestDTO {

    public static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    @Pattern(regexp = EMAIL_REGEX, message = "E-mail inválido")
    private String email;

    @NotBlank(message = "A matrícula é obrigatória")
    @Size(max = 20, message = "A matrícula deve ter no máximo 20 caracteres")
    private String matricula;

    @NotNull(message = "O perfil é obrigatório")
    private PerfilUsuario perfil;

    //opcional: se não vier, o usuário é cadastrado como ativo
    private Boolean ativo;
}
