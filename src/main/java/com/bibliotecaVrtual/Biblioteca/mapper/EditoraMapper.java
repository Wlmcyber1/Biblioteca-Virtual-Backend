package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.EditoraModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditoraMapper {

    //transforma dto em model
    @Mapping(target = "id", ignore = true)
    EditoraModel toModel(EditoraRequestDTO dto);

    //transforma model pra dto
    EditoraResponseDTO toDTO(EditoraModel model);
}
