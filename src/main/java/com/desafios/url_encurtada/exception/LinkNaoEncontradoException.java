package com.desafios.url_encurtada.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LinkNaoEncontradoException extends NegocioException {

    private final String detail;

    public LinkNaoEncontradoException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Link não encontrado.");
        problemDetail.setDetail(detail);

        return problemDetail;
    }
}
