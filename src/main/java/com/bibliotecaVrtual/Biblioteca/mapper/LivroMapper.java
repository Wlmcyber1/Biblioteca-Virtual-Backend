package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CategoriaMapper.class)
public interface LivroMapper {

    //transforma dto em model (a categoria é buscada pelo id no service)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    LivroModel toModel(LivroRequestDTO dto);

    //transforma model pra dto
    LivroResponseDTO toDTO(LivroModel model);
}
