package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.EditoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editoras")
@RequiredArgsConstructor
public class EditoraController {

    private final EditoraService editoraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditoraResponseDTO cadastrar(@Valid @RequestBody EditoraRequestDTO dto) {
        return editoraService.cadastrar(dto);
    }

    @GetMapping
    public List<EditoraResponseDTO> listarTodas() {
        return editoraService.listarTodas();
    }

    @GetMapping("/{id}")
    public EditoraResponseDTO buscarPorId(@PathVariable Long id) {
        return editoraService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        editoraService.excluir(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    public EditoraResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody EditoraRequestDTO dto) {
        return editoraService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    public EditoraResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody EditoraRequestDTO dto) {
        return editoraService.atualizarPorId(id, dto);
    }
}
