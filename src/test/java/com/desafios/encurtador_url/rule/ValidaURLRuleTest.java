package com.desafios.encurtador_url.rule;

import com.desafios.encurtador_url.exception.URLInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidaURLRuleTest {

    private ValidaURLRule validaURLRule;

    @BeforeEach
    void init() {
        validaURLRule = new ValidaURLRule();
    }

    @Test
    @DisplayName("Valida URLs válidas.")
    void testValidarURLComSucesso() {
        List<String> urlsValidas = List.of(
                "http://youtube.com",
                "https://youtube.com",
                "http://www.examplo.com",
                "https://sub.dominio.com/path?query=1"
        );

        urlsValidas.forEach(url -> assertDoesNotThrow(() -> validaURLRule.validar(url),
                String.format("A validação falhou para a URL válida: %s", url))
        );
    }

    @Test
    @DisplayName("Valida URLs inválidas.")
    void testValidarURLsInvalidas() {
        List<String> urlsInvalidas = List.of(
                "ftp://example.com",
                "http//sem-dois-pontos.com",
                "https//sem-dois-pontos.com",
                "http://",
                "://sem-protocolo.com",
                "",
                "    "
        );

        urlsInvalidas.forEach(url ->
                assertThrows(URLInvalidaException.class,
                        () -> validaURLRule.validar(url),
                        String.format("A validação não falhou para a URL inválida: %s", url))
        );
    }
}