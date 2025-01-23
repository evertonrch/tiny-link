package com.desafios.url_encurtada.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkUtilsTest {


    @Test
    @DisplayName("teste se o resultado realmente é do tamanho esperado (5).")
    void testGeraAleatoriosAlfanumericos() {
        int tamanho = 5;

        String resultado = LinkUtils.geraAleatoriosAlfanumericos();

        assertEquals(tamanho, resultado.length());
    }

    @Test
    @DisplayName("teste se realmente é uma string de alfanúmericos")
    void testGeraAleatoriosSomenteComAlfanumericos() {
        String regex = "^[a-zA-Z0-9]+$";

        String resultado = LinkUtils.geraAleatoriosAlfanumericos();

        assertTrue(resultado.matches(regex));
    }

    @Test
    @DisplayName("testa se o tamanho for diferente do esperado.")
    void testGeraAleatoriosAlfanumericosComTamanhoIncorreto() {
        int tamanho = 5;

        String resultado = LinkUtils.geraAleatoriosAlfanumericos();

        assertFalse(resultado.length() > tamanho);
        assertFalse(resultado.length() < tamanho);
    }
}