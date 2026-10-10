package com.bibliotecaVrtual.Biblioteca.service;

import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroRequestDTO;
import com.bibliotecaVrtual.Biblioteca.dto.LivroDTO.LivroResponseDTO;
import com.bibliotecaVrtual.Biblioteca.mapper.LivroMapper;
import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import com.bibliotecaVrtual.Biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    private final LivroMapper livroMapper;

    //POST
    public LivroResponseDTO cadastrarLivro (LivroRequestDTO livroRecebido){
        //transformar em model o dto
        LivroModel livro = livroMapper.toModel(livroRecebido);

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
        livroExistente.setAutor(dto.getAutor());

        LivroModel livroSalvo = livroRepository.save(livroExistente);

        return livroMapper.toDTO(livroSalvo);

    }


    //patch

    public LivroResponseDTO atualizarPorId(Long id, LivroRequestDTO dto){
       LivroModel livroExistente = buscarLivroOuErro(id);

       if(dto.getTitulo()!=null){
           livroExistente.setTitulo(dto.getTitulo());
       }

       if(dto.getAutor()!=null){
           livroExistente.setAutor(dto.getAutor());
       }


       LivroModel livroSalvo = livroRepository.save(livroExistente);

       return livroMapper.toDTO(livroSalvo);
    }

    //busca o livro ou devolve 404
    private LivroModel buscarLivroOuErro(Long id){
        return livroRepository.findById(id).orElseThrow(this::livroNaoEncontrado);
    }

    private ResponseStatusException livroNaoEncontrado(){
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
    }
}
