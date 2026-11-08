package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Cadastro de autores")
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um autor")
    public AutorResponseDTO cadastrar(@Valid @RequestBody AutorRequestDTO autor) {
        return autorService.cadastrarAutor(autor);
    }

    @GetMapping
    @Operation(summary = "Lista todos os autores")
    public List<AutorResponseDTO> listarTodos() {
        return autorService.mostrarTodosAutores();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um autor pelo id")
    public AutorResponseDTO buscarPorId(@PathVariable Long id) {
        return autorService.buscarId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um autor")
    public void deletar(@PathVariable Long id) {
        autorService.deletarPorId(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza todos os dados do autor")
    public AutorResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody AutorRequestDTO dto) {
        return autorService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza só os campos enviados")
    public AutorResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody AutorRequestDTO dto) {
        return autorService.atualizarPorId(id, dto);
    }
}
