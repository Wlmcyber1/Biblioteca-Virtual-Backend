package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaModel, Long> {

    boolean existsBySerieAndLetraAndAnoLetivo(Integer serie, String letra, Integer anoLetivo);

    boolean existsBySerieAndLetraAndAnoLetivoAndIdNot(Integer serie, String letra, Integer anoLetivo, Long id);
}
