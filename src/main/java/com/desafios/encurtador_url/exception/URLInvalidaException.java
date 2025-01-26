package com.desafios.encurtador_url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class URLInvalidaException extends NegocioException {

    private final String detail;

    public URLInvalidaException(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(detail);

        return problemDetail;
    }
}
