package com.bibliotecaVrtual.Biblioteca.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tratamento global de erros: toda exceção vira { status, mensagem, erros[] }.
 * 400 (validação), 404 (não encontrado), 409 (conflito no banco) e 500 (inesperado).
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //códigos de erro do PostgreSQL
    private static final String FK_VIOLATION = "23503";
    private static final String UNIQUE_VIOLATION = "23505";

    //Postgres avisa: Key (id)=(1) is still referenced from table "tb_livro".
    private static final Pattern TABELA_REFERENCIADA =
            Pattern.compile("still referenced from table \"([^\"]+)\"");

    //nome da tabela -> como aparece na mensagem. Ao criar uma tabela nova, é só adicionar aqui.
    private static final Map<String, String> NOMES_AMIGAVEIS = Map.of(
            "tb_livro", "livros",
            "tb_material", "materiais",
            "tb_autor", "autores",
            "tb_categoria", "categorias",
            "tb_editora", "editoras",
            "tb_turma", "turmas"
    );

    // 400 -> @Valid falhou no corpo da requisição
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(new ErroResponse(400, "Dados inválidos", erros));
    }

    // 400 -> validação em parâmetros (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> tratarConstraintViolation(ConstraintViolationException ex) {
        List<String> erros = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(new ErroResponse(400, "Dados inválidos", erros));
    }

    // 400 -> JSON malformado ou com tipo errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ErroResponse.of(400, "Corpo da requisição inválido ou malformado"));
    }

    // 400 -> /autores/abc em vez de /autores/1
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ErroResponse.of(400, "O parâmetro '" + ex.getName() + "' tem um valor inválido"));
    }

    // 404 -> rota que não existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> tratarRotaInexistente(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErroResponse.of(404, "Recurso não encontrado"));
    }

    // 409 -> o banco recusou a operação (FK, UNIQUE...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarIntegridade(DataIntegrityViolationException ex) {
        String sqlState = buscarSqlState(ex);
        String detalhe = ex.getMostSpecificCause().getMessage();

        String mensagem;
        if (FK_VIOLATION.equals(sqlState)) {
            mensagem = mensagemDeVinculo(detalhe);
        } else if (UNIQUE_VIOLATION.equals(sqlState)) {
            mensagem = "Já existe um registro com esses dados";
        } else {
            log.warn("Violação de integridade não mapeada: {}", detalhe);
            mensagem = "A operação viola uma regra de integridade dos dados";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErroResponse.of(409, mensagem));
    }

    // 404, 405, 415... -> exceções do próprio Spring (inclui ResponseStatusException) mantêm o status delas
    // 500 -> qualquer outra coisa que não foi prevista
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarGenerico(Exception ex) {
        if (ex instanceof ErrorResponse erro) {
            HttpStatusCode status = erro.getStatusCode();
            String mensagem = erro.getBody().getDetail();
            if (mensagem == null || mensagem.isBlank()) {
                mensagem = HttpStatus.valueOf(status.value()).getReasonPhrase();
            }
            return ResponseEntity.status(status)
                    .body(ErroResponse.of(status.value(), mensagem));
        }

        //o detalhe fica só no log, nunca vai pro cliente
        log.error("Erro inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErroResponse.of(500, "Erro interno do servidor"));
    }

    // ---------- auxiliares ----------

    //monta "Não é possível excluir: existem livros vinculados a este registro"
    private String mensagemDeVinculo(String detalhe) {
        if (detalhe != null) {
            Matcher m = TABELA_REFERENCIADA.matcher(detalhe);
            if (m.find()) {
                String tabela = m.group(1);
                String nome = NOMES_AMIGAVEIS.getOrDefault(tabela, "registros");
                return "Não é possível excluir: existem " + nome + " vinculados a este registro";
            }
        }
        return "Não é possível concluir a operação: existem registros vinculados a este item";
    }

    //desce a cadeia de causas até achar o SQLException e devolve o código do erro
    private String buscarSqlState(Throwable ex) {
        Throwable atual = ex;
        while (atual != null) {
            if (atual instanceof SQLException sql && sql.getSQLState() != null) {
                return sql.getSQLState();
            }
            atual = atual.getCause();
        }
        return null;
    }
}
