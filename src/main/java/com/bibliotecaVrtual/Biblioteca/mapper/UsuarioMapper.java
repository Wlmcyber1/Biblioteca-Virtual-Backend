package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.UsuarioModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    //transforma dto em model
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true) //quem preenche é o hibernate
    @Mapping(target = "ativo", defaultValue = "true") //se não vier no JSON, começa ativo
    UsuarioModel toModel(UsuarioRequestDTO dto);

    //transforma model pra dto
    UsuarioResponseDTO toDTO(UsuarioModel model);
}
