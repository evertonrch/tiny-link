package com.desafios.encurtador_url.rule;

import com.desafios.encurtador_url.exception.DimensaoQRCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidaDimensaoQRCodeRuleTest {

    private ValidaDimensaoQRCodeRule validaDimensaoQRCodeRule;

    @BeforeEach
    void setUp() {
        validaDimensaoQRCodeRule = new ValidaDimensaoQRCodeRule();
    }

    @Test
    void deveValidarDimensoesComSucesso() {
        int altura = 100, largura = 200;

        assertDoesNotThrow(() ->
                validaDimensaoQRCodeRule.validar(altura, largura),
                "Não deve lançar exceção com dimensões válidas");

    }

    @Test
    void deveLancarExcecaoParaDimensoesInvalidas() {
        int altura = 100, largura = 0;

        assertThrows(DimensaoQRCodeException.class,
                () -> validaDimensaoQRCodeRule.validar(altura, largura),
                "Deve lançar exceção para qualquer dimensão inválida");

    }
}