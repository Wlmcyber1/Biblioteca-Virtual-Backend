package com.bibliotecaVrtual.Biblioteca.exception;

import java.util.List;

//formato padrão de todas as respostas de erro da API
public record ErroResponse(int status, String mensagem, List<String> erros) {

    public static ErroResponse of(int status, String mensagem) {
        return new ErroResponse(status, mensagem, List.of());
    }
}
