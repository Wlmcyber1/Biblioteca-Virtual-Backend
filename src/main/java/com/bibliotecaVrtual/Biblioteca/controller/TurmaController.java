package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
@Tag(name = "Turmas", description = "Turmas e os livros recomendados pelo professor")
public class TurmaController {

    private final TurmaService turmaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma turma com professor e livros recomendados")
    public TurmaResponseDTO cadastrar(@Valid @RequestBody TurmaRequestDTO dto) {
        return turmaService.cadastrar(dto);
    }

    @GetMapping
    @Operation(summary = "Lista todas as turmas")
    public List<TurmaResponseDTO> listarTodas() {
        return turmaService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma turma pelo id")
    public TurmaResponseDTO buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma turma (as recomendações dela saem junto)")
    public void excluir(@PathVariable Long id) {
        turmaService.excluir(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza todos os dados da turma (substitui a lista de livros)")
    public TurmaResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody TurmaRequestDTO dto) {
        return turmaService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza só os campos enviados")
    public TurmaResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody TurmaRequestDTO dto) {
        return turmaService.atualizarPorId(id, dto);
    }
}
