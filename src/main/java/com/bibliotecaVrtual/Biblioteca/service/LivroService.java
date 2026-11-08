package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.LivroMapper;
import com.bibliotecaVrtual.Biblioteca.model.AutorModel;
import com.bibliotecaVrtual.Biblioteca.model.CategoriaModel;
import com.bibliotecaVrtual.Biblioteca.model.EditoraModel;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import com.bibliotecaVrtual.Biblioteca.repository.AutorRepository;
import com.bibliotecaVrtual.Biblioteca.repository.CategoriaRepository;
import com.bibliotecaVrtual.Biblioteca.repository.EditoraRepository;
import com.bibliotecaVrtual.Biblioteca.repository.LivroRepository;
import com.bibliotecaVrtual.Biblioteca.repository.TurmaRepository;
import com.bibliotecaVrtual.Biblioteca.repository.specification.LivroSpecification;
import com.bibliotecaVrtual.Biblioteca.repository.specification.TurmaSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    private final TurmaRepository turmaRepository;

    private final LivroMapper livroMapper;

    private final CategoriaRepository categoriaRepository;

    private final EditoraRepository editoraRepository;

    private final AutorRepository autorRepository;

    //POST
    public LivroResponseDTO cadastrarLivro (LivroRequestDTO livroRecebido){
        //transformar em model o dto
        LivroModel livro = livroMapper.toModel(livroRecebido);
        livro.setCategoria(buscarCategoriaOuErro(livroRecebido.getCategoriaId()));
        livro.setEditora(buscarEditoraOuNulo(livroRecebido.getEditoraId()));
        livro.setAutores(buscarAutoresOuErro(livroRecebido.getAutorIds()));

        // e salvar no banco de dados
        LivroModel livroSalvo = livroRepository.save(livro);

       return livroMapper.toDTO(livroSalvo);
    }


    //GET
    public List<LivroResponseDTO> mostrarTodosLivros() {
        List<LivroModel> livros = livroRepository.findAll();

        return livros.stream()
                .map(livroMapper::toDTO).toList();
    }

    //Get por id
    public LivroResponseDTO buscarId(Long id){
        LivroModel livro = buscarLivroOuErro(id);

        return livroMapper.toDTO(livro);
    }

    //busca avançada: junta com AND só os filtros que vieram preenchidos
    public List<LivroResponseDTO> buscaAvancada(String titulo, String autor, Long categoriaId,
                                                Long editoraId, Integer anoMin, Integer anoMax) {
        if (anoMin != null && anoMax != null && anoMin > anoMax) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "anoMin não pode ser maior que anoMax");
        }
        Specification<LivroModel> filtros = Specification.allOf(
                LivroSpecification.tituloContem(titulo),
                LivroSpecification.autorContem(autor),
                LivroSpecification.daCategoria(categoriaId),
                LivroSpecification.daEditora(editoraId),
                LivroSpecification.anoMinimo(anoMin),
                LivroSpecification.anoMaximo(anoMax));

        return livroRepository.findAll(filtros, Sort.by("titulo")).stream()
                .map(livroMapper::toDTO).toList();
    }

    //livros recomendados: acha as turmas que batem com os filtros e junta os livros delas, sem repetir
    public List<LivroResponseDTO> buscarRecomendados(Long turmaId, Integer serie, String letra,
                                                     Integer anoLetivo, String professor) {
        Specification<TurmaModel> filtros = Specification.allOf(
                TurmaSpecification.comId(turmaId),
                TurmaSpecification.daSerie(serie),
                TurmaSpecification.daLetra(letra),
                TurmaSpecification.doAnoLetivo(anoLetivo),
                TurmaSpecification.professorContem(professor));

        return turmaRepository.findAll(filtros).stream()
                .flatMap(turma -> turma.getLivros().stream())
                .distinct() //o mesmo livro pode ter sido recomendado em mais de uma turma
                .sorted(Comparator.comparing(LivroModel::getTitulo))
                .map(livroMapper::toDTO).toList();
    }

    //delete
    public void deletarPorId(Long id){
        if(!livroRepository.existsById(id)){
            throw livroNaoEncontrado();
        }

         livroRepository.deleteById(id);

    }

    //put -> altera tudo

    public LivroResponseDTO atualizarTudo(Long id, LivroRequestDTO dto){
        LivroModel livroExistente = buscarLivroOuErro(id);

        livroExistente.setTitulo(dto.getTitulo());
        livroExistente.setAutores(buscarAutoresOuErro(dto.getAutorIds()));
        livroExistente.setCategoria(buscarCategoriaOuErro(dto.getCategoriaId()));
        livroExistente.setEditora(buscarEditoraOuNulo(dto.getEditoraId()));
        livroExistente.setDescricao(dto.getDescricao());
        livroExistente.setAnoPublicacao(dto.getAnoPublicacao());
        livroExistente.setUrlArquivo(dto.getUrlArquivo());

        LivroModel livroSalvo = livroRepository.save(livroExistente);

        return livroMapper.toDTO(livroSalvo);

    }


    //patch

    public LivroResponseDTO atualizarPorId(Long id, LivroRequestDTO dto){
       LivroModel livroExistente = buscarLivroOuErro(id);

       if(dto.getTitulo()!=null){
           livroExistente.setTitulo(dto.getTitulo());
       }

       //só troca os autores se a lista vier; lista vazia ou com id nulo é recusada (no PATCH não tem @Valid)
       if(dto.getAutorIds()!=null){
           if(dto.getAutorIds().isEmpty() || dto.getAutorIds().contains(null)){
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe pelo menos um autor, sem ids nulos");
           }
           livroExistente.setAutores(buscarAutoresOuErro(dto.getAutorIds()));
       }

       if(dto.getCategoriaId()!=null){
           livroExistente.setCategoria(buscarCategoriaOuErro(dto.getCategoriaId()));
       }

       if(dto.getEditoraId()!=null){
           livroExistente.setEditora(buscarEditoraOuNulo(dto.getEditoraId()));
       }

       if(dto.getDescricao()!=null){
           livroExistente.setDescricao(dto.getDescricao());
       }

       if(dto.getAnoPublicacao()!=null){
           livroExistente.setAnoPublicacao(dto.getAnoPublicacao());
       }

       if(dto.getUrlArquivo()!=null){
           livroExistente.setUrlArquivo(dto.getUrlArquivo());
       }

       LivroModel livroSalvo = livroRepository.save(livroExistente);

       return livroMapper.toDTO(livroSalvo);
    }

    //busca o livro ou devolve 404
    private LivroModel buscarLivroOuErro(Long id){
        return livroRepository.findById(id).orElseThrow(this::livroNaoEncontrado);
    }

    //busca a categoria pelo id ou devolve 404
    private CategoriaModel buscarCategoriaOuErro(Long categoriaId){
        return categoriaRepository.findById(categoriaId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    //busca a editora pelo id ou devolve 404; sem id, o livro fica sem editora
    private EditoraModel buscarEditoraOuNulo(Long editoraId){
        if(editoraId == null){
            return null;
        }
        return editoraRepository.findById(editoraId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Editora não encontrada"));
    }

    //busca os autores pelos ids ou devolve 404 se algum não existir; ids repetidos são ignorados
    private List<AutorModel> buscarAutoresOuErro(List<Long> autorIds){
        List<Long> idsSemRepetir = autorIds.stream().distinct().toList();

        List<AutorModel> autores = autorRepository.findAllById(idsSemRepetir);

        if(autores.size() != idsSemRepetir.size()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado");
        }
        return new ArrayList<>(autores);
    }

    private ResponseStatusException livroNaoEncontrado(){
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
    }
}
