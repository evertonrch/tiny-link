package com.desafios.encurtador_url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class QRCodeNaoGeradoException extends NegocioException {

    private final String detail;
    private final Throwable throwable;

    public QRCodeNaoGeradoException(String message, Throwable throwable) {
        this.detail = message;
        this.throwable = throwable;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(detail);
        problemDetail.setProperty("message", throwable.getMessage());
        problemDetail.setProperty("cause", throwable.getCause().toString());

        return problemDetail;
    }
}
