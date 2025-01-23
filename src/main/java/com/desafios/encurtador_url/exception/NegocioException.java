package com.desafios.encurtador_url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class NegocioException extends RuntimeException {

    private final String MENSAGEM_ERRO = "Um erro inesperado ocorreu. Informe o administrador.";

    public ProblemDetail toProblemDetail() {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        detail.setDetail(MENSAGEM_ERRO);

        return detail;
    }
}
