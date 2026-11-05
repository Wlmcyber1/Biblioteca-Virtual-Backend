package com.bibliotecaVrtual.Biblioteca.repository.specification;

import com.bibliotecaVrtual.Biblioteca.model.TurmaModel;
import org.springframework.data.jpa.domain.Specification;

//filtros para achar as turmas cujos livros recomendados o aluno quer ver
public class TurmaSpecification {

    public static Specification<TurmaModel> comId(Long turmaId) {
        return (root, query, cb) -> turmaId == null ? null : cb.equal(root.get("id"), turmaId);
    }

    public static Specification<TurmaModel> daSerie(Integer serie) {
        return (root, query, cb) -> serie == null ? null : cb.equal(root.get("serie"), serie);
    }

    public static Specification<TurmaModel> daLetra(String letra) {
        return (root, query, cb) -> letra == null || letra.isBlank() ? null
                : cb.equal(cb.upper(root.get("letra")), letra.toUpperCase());
    }

    public static Specification<TurmaModel> doAnoLetivo(Integer anoLetivo) {
        return (root, query, cb) -> anoLetivo == null ? null : cb.equal(root.get("anoLetivo"), anoLetivo);
    }

    public static Specification<TurmaModel> professorContem(String nome) {
        return (root, query, cb) -> nome == null || nome.isBlank() ? null
                : cb.like(cb.lower(root.get("professor").get("nome")), "%" + nome.toLowerCase() + "%");
    }
}
