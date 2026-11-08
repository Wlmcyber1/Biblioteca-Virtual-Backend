package com.bibliotecaVrtual.Biblioteca.repository;

import com.bibliotecaVrtual.Biblioteca.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    boolean existsByEmailIgnoreCase(String email); //se existe email
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id); // esse email existe em outro usuario

    boolean existsByMatricula(String matricula);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);
}
