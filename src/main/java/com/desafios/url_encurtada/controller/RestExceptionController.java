package com.desafios.url_encurtada.controller;

import com.desafios.url_encurtada.exception.NegocioException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionController {

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocioException(NegocioException ex) {
        return ex.toProblemDetail();
    }
}
