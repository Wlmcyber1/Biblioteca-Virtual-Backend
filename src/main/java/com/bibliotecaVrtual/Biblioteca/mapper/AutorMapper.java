package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.AutorModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    //transforma dto em model
    AutorModel toModel(AutorRequestDTO dto);

    //transforma model pra dto
    AutorResponseDTO toDTO(AutorModel model);
}
