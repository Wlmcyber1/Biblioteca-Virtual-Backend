package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.TurmaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService turmaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurmaResponseDTO cadastrar(@Valid @RequestBody TurmaRequestDTO dto) {
        return turmaService.cadastrar(dto);
    }

    @GetMapping
    public List<TurmaResponseDTO> listarTodas() {
        return turmaService.listarTodas();
    }

    @GetMapping("/{id}")
    public TurmaResponseDTO buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        turmaService.excluir(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    public TurmaResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody TurmaRequestDTO dto) {
        return turmaService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    public TurmaResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody TurmaRequestDTO dto) {
        return turmaService.atualizarPorId(id, dto);
    }
}
