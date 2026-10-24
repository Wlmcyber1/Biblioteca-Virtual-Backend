package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.AutorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<AutorModel, Long> {
}
