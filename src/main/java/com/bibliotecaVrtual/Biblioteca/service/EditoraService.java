package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.EditoraMapper;
import com.bibliotecaVrtual.Biblioteca.model.EditoraModel;
import com.bibliotecaVrtual.Biblioteca.repository.EditoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditoraService {

    private final EditoraRepository editoraRepository;

    private final EditoraMapper editoraMapper;

    //POST
    public EditoraResponseDTO cadastrar(EditoraRequestDTO dto) {
        EditoraModel editora = editoraMapper.toModel(dto);

        return editoraMapper.toDTO(editoraRepository.save(editora));
    }

    //GET
    public List<EditoraResponseDTO> listarTodas() {
        return editoraRepository.findAll().stream()
                .map(editoraMapper::toDTO).toList();
    }

    //GET por id
    public EditoraResponseDTO buscarPorId(Long id) {
        return editoraMapper.toDTO(buscarOuErro(id));
    }

    //DELETE
    //se a editora tiver materiais ligados, o banco rejeita e o GlobalExceptionHandler devolve 409
    public void excluir(Long id) {
        if (!editoraRepository.existsById(id)) {
            throw naoEncontrada();
        }

        editoraRepository.deleteById(id);
    }

    //PUT -> altera tudo
    public EditoraResponseDTO atualizarTudo(Long id, EditoraRequestDTO dto) {
        EditoraModel existente = buscarOuErro(id);

        existente.setNome(dto.getNome());
        existente.setCnpj(dto.getCnpj());
        existente.setEmail(dto.getEmail());

        return editoraMapper.toDTO(editoraRepository.save(existente));
    }

    //PATCH -> altera só os campos enviados
    //no PATCH não tem @Valid (campos podem vir nulos), então as mesmas regras são barradas aqui
    public EditoraResponseDTO atualizarPorId(Long id, EditoraRequestDTO dto) {
        EditoraModel existente = buscarOuErro(id);

        if (dto.getNome() != null) {
            if (dto.getNome().isBlank() || dto.getNome().length() > 150) {
                throw invalido("O nome deve ter de 1 a 150 caracteres");
            }
            existente.setNome(dto.getNome());
        }

        if (dto.getCnpj() != null) {
            if (!dto.getCnpj().matches(EditoraRequestDTO.CNPJ_REGEX)) {
                throw invalido("CNPJ inválido");
            }
            existente.setCnpj(dto.getCnpj());
        }

        if (dto.getEmail() != null) {
            if (dto.getEmail().length() > 150 || !dto.getEmail().matches(EditoraRequestDTO.EMAIL_REGEX)) {
                throw invalido("E-mail inválido ou maior que 150 caracteres");
            }
            existente.setEmail(dto.getEmail());
        }

        return editoraMapper.toDTO(editoraRepository.save(existente));
    }

    //busca a editora ou devolve 404
    private EditoraModel buscarOuErro(Long id) {
        return editoraRepository.findById(id).orElseThrow(this::naoEncontrada);
    }

    private ResponseStatusException naoEncontrada() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada");
    }

    private ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
