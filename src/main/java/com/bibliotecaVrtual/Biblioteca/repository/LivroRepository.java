package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//JpaSpecificationExecutor libera o findAll(Specification), usado na busca avançada
@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long>, JpaSpecificationExecutor<LivroModel> {
}
