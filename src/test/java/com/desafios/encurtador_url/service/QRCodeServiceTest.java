package com.desafios.encurtador_url.service;

import com.desafios.encurtador_url.exception.DimensaoQRCodeException;
import com.desafios.encurtador_url.exception.URLInvalidaException;
import com.desafios.encurtador_url.rule.ValidaDimensaoQRCodeRule;
import com.desafios.encurtador_url.rule.ValidaURLRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QRCodeServiceTest {

    @InjectMocks
    private QRCodeService qrCodeService;
    @Mock
    private ValidaURLRule validaURLRule;
    @Mock
    private ValidaDimensaoQRCodeRule validaDimensaoQRCodeRule;

    @Test
    void deveGerarQRCodeComSucesso() {
        String url = "https://example.com";
        int largura = 200;
        int altura = 200;

        byte[] qrCode = qrCodeService.gerarQRCode(url, largura, altura);

        assertNotNull(qrCode, "O conteúdo do QR Code não dever ser nulo");
        assertTrue(qrCode.length > 0, "O QR Code deve ter conteúdo");
    }

    @Test
    void deveGerarQRCodeBase64ComSucesso() {
        String url = "https://example.com";
        int largura = 200;
        int altura = 200;

        String qrCodeBase64 = qrCodeService.gerarQRCodeBase64(url, largura, altura);

        assertNotNull(qrCodeBase64, "O QR Code em Base64 não deve ser nulo");
        assertFalse(qrCodeBase64.isBlank(), "O QR Code em Base64 não deve estar vazio");

        byte[] decodedBytes = Base64.getDecoder().decode(qrCodeBase64);
        assertTrue(decodedBytes.length > 0, "Os bytes decodificados do QR Code devem ter conteúdo");
    }

    @Test
    void deveLancarExcecaoQuandoDimensoesSaoInvalidas() {
        String url = "https://example.com";
        int largura = 0;
        int altura = -1;
        String mensagemExcecao = "Falha ao gerar o QRCode.";

        doThrow(new DimensaoQRCodeException(mensagemExcecao))
                .when(validaDimensaoQRCodeRule)
                .validar(altura, largura);

        DimensaoQRCodeException ex = assertThrows(DimensaoQRCodeException.class, () -> qrCodeService.gerarQRCode(url, largura, altura));

        assertThat(mensagemExcecao, containsString(ex.toProblemDetail().getDetail()));

        verify(validaDimensaoQRCodeRule, times(1)).validar(altura, largura);
        verify(validaURLRule, times(1)).validar(url);
    }

    @Test
    void deveLancarExcecaoQuandoUrlForInvalida() {
        String urlInvalida = "";
        int largura = 200;
        int altura = 200;
        String mensagemException = "A URL fornecida não é válida";

        doThrow(new URLInvalidaException(mensagemException))
                .when(validaURLRule)
                .validar(urlInvalida);

        URLInvalidaException ex = assertThrows(URLInvalidaException.class, () -> {
            qrCodeService.gerarQRCode(urlInvalida, largura, altura);
        });

        assertThat(mensagemException, is(ex.toProblemDetail().getDetail()));

        verify(validaURLRule, times(1)).validar(urlInvalida);
        verifyNoInteractions(validaDimensaoQRCodeRule);
    }
}