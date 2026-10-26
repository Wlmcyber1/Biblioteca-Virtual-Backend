package com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO;

import com.bibliotecaVrtual.Biblioteca.model.PerfilUsuario;
import lombok.*;

import java.time.LocalDateTime;

//O QUE A API DEVOLVE
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String matricula;
    private PerfilUsuario perfil;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}
