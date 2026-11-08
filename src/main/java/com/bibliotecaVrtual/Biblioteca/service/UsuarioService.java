package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.UsuarioDTO.UsuarioResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.UsuarioMapper;
import com.bibliotecaVrtual.Biblioteca.model.UsuarioModel;
import com.bibliotecaVrtual.Biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    //POST
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        String email = normalizarEmail(dto.getEmail());
        String matricula = dto.getMatricula().trim();
        validarEmailUnico(email, null);
        validarMatriculaUnica(matricula, null);

        UsuarioModel usuario = usuarioMapper.toModel(dto);
        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setMatricula(matricula);

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    //GET
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO).toList();
    }

    //GET por id
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return usuarioMapper.toDTO(buscarOuErro(id));
    }

    //PUT -> altera tudo
    public UsuarioResponseDTO atualizarTudo(Long id, UsuarioRequestDTO dto) {
        UsuarioModel usuario = buscarOuErro(id);

        String email = normalizarEmail(dto.getEmail());
        String matricula = dto.getMatricula().trim();
        validarEmailUnico(email, id);
        validarMatriculaUnica(matricula, id);

        usuario.setNome(dto.getNome().trim());
        usuario.setEmail(email);
        usuario.setMatricula(matricula);
        usuario.setPerfil(dto.getPerfil());
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    //PATCH -> altera só os campos enviados
    //no PATCH não tem @Valid (campos podem vir nulos), então as mesmas regras são barradas aqui
    public UsuarioResponseDTO atualizarPorId(Long id, UsuarioRequestDTO dto) {
        UsuarioModel usuario = buscarOuErro(id);

        if (dto.getNome() != null) {
            String nome = dto.getNome().trim();
            if (nome.isEmpty() || nome.length() > 150) {
                throw invalido("O nome deve ter de 1 a 150 caracteres");
            }
            usuario.setNome(nome);
        }

        if (dto.getEmail() != null) {
            String email = normalizarEmail(dto.getEmail());
            if (email.length() > 150 || !email.matches(UsuarioRequestDTO.EMAIL_REGEX)) {
                throw invalido("E-mail inválido ou maior que 150 caracteres");
            }
            validarEmailUnico(email, id);
            usuario.setEmail(email);
        }

        if (dto.getMatricula() != null) {
            String matricula = dto.getMatricula().trim();
            if (matricula.isEmpty() || matricula.length() > 20) {
                throw invalido("A matrícula deve ter de 1 a 20 caracteres");
            }
            validarMatriculaUnica(matricula, id);
            usuario.setMatricula(matricula);
        }

        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }

        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    //DELETE
    public void excluir(Long id) {
        UsuarioModel usuario = buscarOuErro(id);
        usuarioRepository.delete(usuario);
    }

    //busca o usuário ou devolve 404
    private UsuarioModel buscarOuErro(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    //idAtual null = cadastro; com id = edição (ignora o próprio usuário)
    private void validarEmailUnico(String email, Long idAtual) {
        boolean duplicado = idAtual == null
                ? usuarioRepository.existsByEmailIgnoreCase(email)
                : usuarioRepository.existsByEmailIgnoreCaseAndIdNot(email, idAtual);
        if (duplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }
    }

    private void validarMatriculaUnica(String matricula, Long idAtual) {
        boolean duplicado = idAtual == null
                ? usuarioRepository.existsByMatricula(matricula)
                : usuarioRepository.existsByMatriculaAndIdNot(matricula, idAtual);
        if (duplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com esta matrícula");
        }
    }

    //" Maria@Escola.com " vira "maria@escola.com"
    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }

    private ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
