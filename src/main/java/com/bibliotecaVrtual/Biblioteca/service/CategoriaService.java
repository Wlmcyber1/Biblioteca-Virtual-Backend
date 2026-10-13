package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.model.CategoriaModel;
import com.bibliotecaVrtual.Biblioteca.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaResponseDTO cadastrar(CategoriaRequestDTO dto) {
        String nome = normalizarNome(dto.getNome());
        validarNomeUnico(nome, null);
        CategoriaModel categoria = new CategoriaModel(null, nome);
        return paraResponse(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream().map(this::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return paraResponse(buscarOuErro(id));
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        CategoriaModel categoria = buscarOuErro(id);
        String nome = normalizarNome(dto.getNome());
        validarNomeUnico(nome, id);
        categoria.setNome(nome);
        return paraResponse(categoriaRepository.save(categoria));
    }

    public void excluir(Long id) {
        CategoriaModel categoria = buscarOuErro(id);
        categoriaRepository.delete(categoria);
    }

    private CategoriaModel buscarOuErro(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    private void validarNomeUnico(String nome, Long idAtual) {
        boolean duplicado = idAtual == null
                ? categoriaRepository.existsByNomeIgnoreCase(nome)
                : categoriaRepository.existsByNomeIgnoreCaseAndIdNot(nome, idAtual);
        if (duplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria com esse nome");
        }
    }

    private String normalizarNome(String nome) {
        return nome.trim().replaceAll("\s+", " ");
    }

    private CategoriaResponseDTO paraResponse(CategoriaModel categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }
}
