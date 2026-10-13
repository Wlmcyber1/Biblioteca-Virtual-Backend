package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.CategoriaDTO.CategoriaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO cadastrar(@Valid @RequestBody CategoriaRequestDTO dto) {
        return categoriaService.cadastrar(dto);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        return categoriaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
    }
}
