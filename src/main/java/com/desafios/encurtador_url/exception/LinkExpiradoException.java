package com.desafios.encurtador_url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LinkExpiradoException extends LinkNaoEncontradoException {

    public LinkExpiradoException(String detail) {
        super(detail);
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = super.toProblemDetail();
        problemDetail.setTitle("Link expirado.");
        problemDetail.setStatus(HttpStatus.GONE);

        return problemDetail;
    }
}
