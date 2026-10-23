package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.AutorMapper;
import com.bibliotecaVrtual.Biblioteca.model.AutorModel;
import com.bibliotecaVrtual.Biblioteca.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private final AutorMapper autorMapper;

    //POST
    public AutorResponseDTO cadastrarAutor(AutorRequestDTO autorRecebido) {
        AutorModel autor = autorMapper.toModel(autorRecebido);

        AutorModel autorSalvo = autorRepository.save(autor);

        return autorMapper.toDTO(autorSalvo);
    }

    //GET
    public List<AutorResponseDTO> mostrarTodosAutores() {
        return autorRepository.findAll().stream()
                .map(autorMapper::toDTO).toList();
    }

    //GET por id
    public AutorResponseDTO buscarId(Long id) {
        return autorMapper.toDTO(buscarAutorOuErro(id));
    }

    //DELETE
    //se o autor tiver livros/materiais ligados, o banco rejeita e o GlobalExceptionHandler devolve 409
    public void deletarPorId(Long id) {
        if (!autorRepository.existsById(id)) {
            throw autorNaoEncontrado();
        }

        autorRepository.deleteById(id);
    }

    //PUT -> altera tudo
    public AutorResponseDTO atualizarTudo(Long id, AutorRequestDTO dto) {
        AutorModel autorExistente = buscarAutorOuErro(id);

        autorExistente.setNome(dto.getNome());
        autorExistente.setNacionalidade(dto.getNacionalidade());

        return autorMapper.toDTO(autorRepository.save(autorExistente));
    }

    //PATCH -> altera só os campos enviados
    public AutorResponseDTO atualizarPorId(Long id, AutorRequestDTO dto) {
        AutorModel autorExistente = buscarAutorOuErro(id);

        if (dto.getNome() != null) {
            //no PATCH não tem @Valid, então o nome em branco é barrado aqui
            if (dto.getNome().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome não pode ficar em branco");
            }
            autorExistente.setNome(dto.getNome());
        }

        if (dto.getNacionalidade() != null) {
            autorExistente.setNacionalidade(dto.getNacionalidade());
        }

        return autorMapper.toDTO(autorRepository.save(autorExistente));
    }

    //busca o autor ou devolve 404
    private AutorModel buscarAutorOuErro(Long id) {
        return autorRepository.findById(id).orElseThrow(this::autorNaoEncontrado);
    }

    private ResponseStatusException autorNaoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado");
    }
}
