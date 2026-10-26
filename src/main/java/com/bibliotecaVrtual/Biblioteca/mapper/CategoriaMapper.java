package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.CategoriaModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    //transforma dto em model
    CategoriaModel toModel(CategoriaRequestDTO dto);

    //transforma model pra dto
    CategoriaResponseDTO toDTO(CategoriaModel model);
}
