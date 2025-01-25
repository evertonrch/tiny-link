package com.desafios.encurtador_url.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class GeracaoQRCodeException extends NegocioException {

    private final String detail;
    private final Throwable throwable;

    public GeracaoQRCodeException(String message, Throwable throwable) {
        this.detail = message;
        this.throwable = throwable;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(detail);
        problemDetail.setProperty("message", ExceptionUtils.getMessage(throwable));
        problemDetail.setProperty("rootCause", ExceptionUtils.getRootCauseMessage(throwable));

        return problemDetail;
    }
}
