package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.EditoraDTO.EditoraResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.EditoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editoras")
@RequiredArgsConstructor
@Tag(name = "Editoras", description = "Cadastro de editoras (CNPJ e e-mail validados)")
public class EditoraController {

    private final EditoraService editoraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma editora")
    public EditoraResponseDTO cadastrar(@Valid @RequestBody EditoraRequestDTO dto) {
        return editoraService.cadastrar(dto);
    }

    @GetMapping
    @Operation(summary = "Lista todas as editoras")
    public List<EditoraResponseDTO> listarTodas() {
        return editoraService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma editora pelo id")
    public EditoraResponseDTO buscarPorId(@PathVariable Long id) {
        return editoraService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma editora (409 se tiver livros)")
    public void excluir(@PathVariable Long id) {
        editoraService.excluir(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza todos os dados da editora")
    public EditoraResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody EditoraRequestDTO dto) {
        return editoraService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza só os campos enviados")
    public EditoraResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody EditoraRequestDTO dto) {
        return editoraService.atualizarPorId(id, dto);
    }
}
