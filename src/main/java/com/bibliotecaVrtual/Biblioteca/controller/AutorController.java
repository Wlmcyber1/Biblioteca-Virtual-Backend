package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.AutorDTO.AutorResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutorResponseDTO cadastrar(@Valid @RequestBody AutorRequestDTO autor) {
        return autorService.cadastrarAutor(autor);
    }

    @GetMapping
    public List<AutorResponseDTO> listarTodos() {
        return autorService.mostrarTodosAutores();
    }

    @GetMapping("/{id}")
    public AutorResponseDTO buscarPorId(@PathVariable Long id) {
        return autorService.buscarId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        autorService.deletarPorId(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    public AutorResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody AutorRequestDTO dto) {
        return autorService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    public AutorResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody AutorRequestDTO dto) {
        return autorService.atualizarPorId(id, dto);
    }
}
