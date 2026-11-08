package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

//JpaSpecificationExecutor libera o findAll(Specification), usado nos livros recomendados
@Repository
public interface TurmaRepository extends JpaRepository<TurmaModel, Long>, JpaSpecificationExecutor<TurmaModel> {

    boolean existsBySerieAndLetraAndAnoLetivo(Integer serie, String letra, Integer anoLetivo);

    boolean existsBySerieAndLetraAndAnoLetivoAndIdNot(Integer serie, String letra, Integer anoLetivo, Long id);
}
