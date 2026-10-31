package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.TurmaDTO.TurmaResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.TurmaMapper;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import com.bibliotecaVrtual.Biblioteca.model.PerfilUsuario;
import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import com.bibliotecaVrtual.Biblioteca.model.UsuarioModel;
import com.bibliotecaVrtual.Biblioteca.repository.LivroRepository;
import com.bibliotecaVrtual.Biblioteca.repository.TurmaRepository;
import com.bibliotecaVrtual.Biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final TurmaMapper turmaMapper;

    //POST
    public TurmaResponseDTO cadastrar(TurmaRequestDTO dto) {
        validar(null, dto.getSerie(), dto.getLetra(), dto.getAnoLetivo(), dto.getProfessorId(), dto.getLivroIds());

        TurmaModel turma = new TurmaModel();
        preencher(turma, dto.getSerie(), dto.getLetra(), dto.getAnoLetivo(), dto.getProfessorId(), dto.getLivroIds());

        return turmaMapper.toDTO(turmaRepository.save(turma));
    }

    //GET
    public List<TurmaResponseDTO> listarTodas() {
        return turmaRepository.findAll().stream().map(turmaMapper::toDTO).toList();
    }

    //GET por id
    public TurmaResponseDTO buscarPorId(Long id) {
        return turmaMapper.toDTO(buscarOuErro(id));
    }

    //DELETE
    public void excluir(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw naoEncontrada();
        }
        turmaRepository.deleteById(id);
    }

    //PUT -> altera tudo (a lista de livros é substituída pela enviada)
    public TurmaResponseDTO atualizarTudo(Long id, TurmaRequestDTO dto) {
        TurmaModel turma = buscarOuErro(id);
        validar(id, dto.getSerie(), dto.getLetra(), dto.getAnoLetivo(), dto.getProfessorId(), dto.getLivroIds());
        preencher(turma, dto.getSerie(), dto.getLetra(), dto.getAnoLetivo(), dto.getProfessorId(), dto.getLivroIds());

        return turmaMapper.toDTO(turmaRepository.save(turma));
    }

    //PATCH -> altera só os campos enviados; a regra é checada sobre o resultado final
    public TurmaResponseDTO atualizarPorId(Long id, TurmaRequestDTO dto) {
        TurmaModel turma = buscarOuErro(id);

        Integer serie = dto.getSerie() != null ? dto.getSerie() : turma.getSerie();
        String letra = dto.getLetra() != null ? dto.getLetra() : turma.getLetra();
        Integer ano = dto.getAnoLetivo() != null ? dto.getAnoLetivo() : turma.getAnoLetivo();
        Long professorId = dto.getProfessorId() != null ? dto.getProfessorId() : turma.getProfessor().getId();
        List<Long> livroIds = dto.getLivroIds() != null
                ? dto.getLivroIds()
                : turma.getLivros().stream().map(LivroModel::getId).toList();

        validar(id, serie, letra, ano, professorId, livroIds);
        preencher(turma, serie, letra, ano, professorId, livroIds);

        return turmaMapper.toDTO(turmaRepository.save(turma));
    }

    //regras: campos válidos, professor existe e é professor, livros existem, sem turma duplicada
    private void validar(Long idAtual, Integer serie, String letra, Integer ano, Long professorId, List<Long> livroIds) {
        if (serie == null || serie < 1 || serie > 3) {
            throw invalido("A série deve ser entre 1 e 3");
        }
        if (letra == null || !letra.matches("[A-Za-z]")) {
            throw invalido("A letra deve ser uma única letra");
        }
        if (ano == null || ano < 2000) {
            throw invalido("O ano letivo deve ser a partir de 2000");
        }

        UsuarioModel professor = usuarioRepository.findById(professorId).orElseThrow(this::professorNaoEncontrado);
        if (professor.getPerfil() != PerfilUsuario.PROFESSOR) {
            throw invalido("O usuário informado não é professor");
        }

        buscarLivros(livroIds);

        String letraMaiuscula = letra.toUpperCase();
        boolean duplicada = idAtual == null
                ? turmaRepository.existsBySerieAndLetraAndAnoLetivo(serie, letraMaiuscula, ano)
                : turmaRepository.existsBySerieAndLetraAndAnoLetivoAndIdNot(serie, letraMaiuscula, ano, idAtual);
        if (duplicada) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma turma com essa série, letra e ano letivo");
        }
    }

    //busca professor e livros e grava os campos no model
    private void preencher(TurmaModel turma, Integer serie, String letra, Integer ano, Long professorId, List<Long> livroIds) {
        UsuarioModel professor = usuarioRepository.findById(professorId).orElseThrow(this::professorNaoEncontrado);

        turma.setSerie(serie);
        turma.setLetra(letra.toUpperCase());
        turma.setAnoLetivo(ano);
        turma.setProfessor(professor);
        turma.setLivros(buscarLivros(livroIds));
    }

    //ids repetidos são ignorados; qualquer id inexistente devolve 404 com o id informado
    private Set<LivroModel> buscarLivros(List<Long> livroIds) {
        if (livroIds == null) {
            throw invalido("A lista de livros é obrigatória (pode ser vazia)");
        }
        return new LinkedHashSet<>(livroIds).stream()
                .map(id -> livroRepository.findById(id).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado: id " + id)))
                .collect(Collectors.toSet());
    }

    private TurmaModel buscarOuErro(Long id) {
        return turmaRepository.findById(id).orElseThrow(this::naoEncontrada);
    }

    private ResponseStatusException naoEncontrada() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada");
    }

    private ResponseStatusException professorNaoEncontrado() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado");
    }

    private ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
