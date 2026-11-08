package com.bibliotecaVrtual.Biblioteca.controller;

import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioResponseDTO;
import com.bibliotecaVrtual.Biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Bibliotecários, professores e estudantes")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra um usuário (e-mail e matrícula únicos)")
    public UsuarioResponseDTO cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.cadastrar(dto);
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo id")
    public UsuarioResponseDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um usuário (409 se for professor de alguma turma)")
    public void excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
    }

    //put -> altera tudo
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza todos os dados do usuário")
    public UsuarioResponseDTO atualizarTudo(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.atualizarTudo(id, dto);
    }

    //patch -> altera só os campos enviados (sem @Valid, pois os campos podem vir nulos)
    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza só os campos enviados")
    public UsuarioResponseDTO atualizarPorId(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.atualizarPorId(id, dto);
    }
}
