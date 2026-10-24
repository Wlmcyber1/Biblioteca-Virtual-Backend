package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.EditoraModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditoraRepository extends JpaRepository<EditoraModel, Long> {
}
