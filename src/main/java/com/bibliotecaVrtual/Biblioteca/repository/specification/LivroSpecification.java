package com.bibliotecaVrtual.Biblioteca.repository.specification;

import com.bibliotecaVrtual.Biblioteca.model.LivroModel;
import org.springframework.data.jpa.domain.Specification;

//cada método é um filtro; se o valor vier nulo/vazio, devolve null e o Spring ignora o filtro
public class LivroSpecification {

    public static Specification<LivroModel> tituloContem(String titulo) {
        return (root, query, cb) -> vazio(titulo) ? null
                : cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<LivroModel> autorContem(String autor) {
        return (root, query, cb) -> vazio(autor) ? null
                : cb.like(cb.lower(root.get("autor")), "%" + autor.toLowerCase() + "%");
    }

    public static Specification<LivroModel> daCategoria(Long categoriaId) {
        return (root, query, cb) -> categoriaId == null ? null
                : cb.equal(root.get("categoria").get("id"), categoriaId);
    }

    public static Specification<LivroModel> daEditora(Long editoraId) {
        return (root, query, cb) -> editoraId == null ? null
                : cb.equal(root.get("editora").get("id"), editoraId);
    }

    public static Specification<LivroModel> anoMinimo(Integer anoMin) {
        return (root, query, cb) -> anoMin == null ? null
                : cb.greaterThanOrEqualTo(root.get("anoPublicacao"), anoMin);
    }

    public static Specification<LivroModel> anoMaximo(Integer anoMax) {
        return (root, query, cb) -> anoMax == null ? null
                : cb.lessThanOrEqualTo(root.get("anoPublicacao"), anoMax);
    }

    private static boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }
}
