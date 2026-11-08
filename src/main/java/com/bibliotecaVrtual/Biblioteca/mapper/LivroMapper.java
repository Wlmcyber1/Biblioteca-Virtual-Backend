package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//utilize o categoriaMapper
@Mapper(componentModel = "spring", uses = {CategoriaMapper.class, EditoraMapper.class, AutorMapper.class})
public interface LivroMapper {

    //transforma dto em model (a categoria, a editora e os autores são buscados pelos ids no service)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "editora", ignore = true)
    @Mapping(target = "autores", ignore = true)
    LivroModel toModel(LivroRequestDTO dto);

    //transforma model pra dto
    LivroResponseDTO toDTO(LivroModel model);
}
