package com.desafios.encurtador_url.rule;

import com.desafios.encurtador_url.exception.URLInvalidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class ValidaURLRule {

    private final Logger log = LoggerFactory.getLogger(ValidaURLRule.class);

    private final String URL_REGEX = "^(https?):\\/\\/[^\s/$.?#].[^\s]*$";
    private final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public void validar(String urlOriginal) {
        if (!ehURLValida(urlOriginal)) {
            log.error("erro ao processar URL: {}", urlOriginal);
            throw new URLInvalidaException("A URL fornecida não é válida");
        }
    }

    private boolean ehURLValida(String urlOriginal) {
        return Optional.ofNullable(urlOriginal)
                .filter(url -> !url.isBlank())
                .filter(url -> URL_PATTERN.matcher(url).matches())
                .isPresent();
    }
}
