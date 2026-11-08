package com.bibliotecaVrtual.Biblioteca.controller;


import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Cadastro, busca avançada e livros recomendados")
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um livro (categoria obrigatória, editora opcional)")
    public LivroResponseDTO cadastrar(@Valid @RequestBody LivroRequestDTO livro)
    {
        return livroService.cadastrarLivro(livro);
    }

    @GetMapping
    @Operation(summary = "Lista todos os livros")
    public List<LivroResponseDTO> listarTodos()
    {
        return livroService.mostrarTodosLivros();
    }

    //busca avançada: todos os filtros são opcionais e podem ser combinados
    @GetMapping("/busca")
    @Operation(summary = "Busca livros com filtros combinados (título, autor, categoria, editora, ano)")
    public List<LivroResponseDTO> buscaAvancada(@RequestParam(required = false) String titulo,
                                                @RequestParam(required = false) String autor,
                                                @RequestParam(required = false) Long categoriaId,
                                                @RequestParam(required = false) Long editoraId,
                                                @RequestParam(required = false) Integer anoMin,
                                                @RequestParam(required = false) Integer anoMax) {
        return livroService.buscaAvancada(titulo, autor, categoriaId, editoraId, anoMin, anoMax);
    }

    //aluno consulta os livros recomendados pela turma ou pelo nome do professor
    @GetMapping("/recomendados")
    @Operation(summary = "Lista os livros recomendados por turma ou pelo nome do professor")
    public List<LivroResponseDTO> recomendados(@RequestParam(required = false) Long turmaId,
                                               @RequestParam(required = false) Integer serie,
                                               @RequestParam(required = false) String letra,
                                               @RequestParam(required = false) Integer anoLetivo,
                                               @RequestParam(required = false) String professor) {
        return livroService.buscarRecomendados(turmaId, serie, letra, anoLetivo, professor);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um livro pelo id")
    public LivroResponseDTO buscarPorId(@PathVariable Long id){
        return livroService.buscarId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um livro (409 se estiver recomendado em alguma turma)")
    public void deletar(@PathVariable Long id){
         livroService.deletarPorId(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza todos os dados do livro")
    public LivroResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto){
       return livroService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza só os campos enviados")
    public LivroResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody LivroRequestDTO dto){
        return livroService.atualizarPorId(id, dto);
    }
}
