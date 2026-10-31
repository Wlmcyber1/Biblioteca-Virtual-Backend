package com.bibliotecaVrtual.Biblioteca.mapper;

import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.LivroResumoDTO;
import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//o model é montado no service, porque professor e livros precisam ser buscados pelo id
@Mapper(componentModel = "spring")
public interface TurmaMapper {

    @Mapping(target = "professorId", source = "professor.id")
    @Mapping(target = "professorNome", source = "professor.nome")
    TurmaResponseDTO toDTO(TurmaModel model);

    LivroResumoDTO toLivroResumo(LivroModel livro);
}
