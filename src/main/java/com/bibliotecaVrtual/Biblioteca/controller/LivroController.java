package com.bibliotecaVrtual.Biblioteca.controller;


import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponseDTO cadastrar(@Valid @RequestBody LivroRequestDTO livro)
    {
        return livroService.cadastrarLivro(livro);
    }

    @GetMapping
    public List<LivroResponseDTO> listarTodos()
    {
        return livroService.mostrarTodosLivros();
    }

    @GetMapping("/{id}")
    public LivroResponseDTO buscarPorId(@PathVariable Long id){
        return livroService.buscarId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id){
         livroService.deletarPorId(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    public LivroResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto){
       return livroService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    public LivroResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody LivroRequestDTO dto){
        return livroService.atualizarPorId(id, dto);
    }
}
