package com.desafios.encurtador_url.service;

import com.desafios.encurtador_url.dto.LinkResponse;
import com.desafios.encurtador_url.exception.GeracaoQRCodeException;
import com.desafios.encurtador_url.exception.LinkExpiradoException;
import com.desafios.encurtador_url.exception.URLInvalidaException;
import com.desafios.encurtador_url.model.Link;
import com.desafios.encurtador_url.repository.LinkRepository;
import com.desafios.encurtador_url.rule.ValidaURLRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @InjectMocks
    private LinkService linkService;
    @Mock
    private LinkRepository linkRepository;
    @Mock
    private QRCodeService qrCodeService;
    @Mock
    private ValidaURLRule validaURLRule;

    @Test
    void deveCriarLinkComSucesso() {
        String urlOriginal = "http://sub.dominio/path";
        int largura = 200, altura = 200;
        String base64QRCode = "base64encodedqrcode";
        String urlEncurtada = "abc123";

        Link link = Link.builder()
                .comUrlOriginal(urlOriginal)
                .comQrcode(base64QRCode)
                .comUrlEncurtada(urlEncurtada)
                .build();

        doReturn(link).when(linkRepository).save(any(Link.class));
        doReturn(base64QRCode).when(qrCodeService).gerarQRCodeBase64(urlOriginal, largura, altura);

        LinkResponse response = linkService.criaLink(urlOriginal);

        assertThat(response, notNullValue());
        assertThat(urlOriginal, is(response.urlOriginal()));
        assertThat(urlEncurtada, is(response.urlEncurtada()));
        assertThat(base64QRCode, is(response.qrcode()));

        verify(validaURLRule, times(1)).validar(urlOriginal);
        verify(qrCodeService, times(1)).gerarQRCodeBase64(urlOriginal, largura, altura);

        ArgumentCaptor<Link> linkCaptor = ArgumentCaptor.forClass(Link.class);
        verify(linkRepository, times(1)).save(linkCaptor.capture());
        Link capturado = linkCaptor.getValue();

        assertThat(urlOriginal, is(capturado.getUrlOriginal()));
        assertThat(base64QRCode, is(capturado.getQrcode()));
        assertThat(capturado.getId(), nullValue());
    }

    @Test
    void naoDeveCriarLinkComUrlInvalida() {
        String urlOriginal = "http:/dominio/path";
        String mensagemException = "A URL fornecida não é válida";

        doThrow(new URLInvalidaException(mensagemException)).when(validaURLRule).validar(urlOriginal);

        URLInvalidaException ex = assertThrows(URLInvalidaException.class, () -> linkService.criaLink(urlOriginal));

        assertThat(mensagemException, is(ex.toProblemDetail().getDetail()));

        verify(validaURLRule, times(1)).validar(urlOriginal);
        verifyNoInteractions(linkRepository, qrCodeService);
    }

    @Test
    void naoDevePersistirLinkQuandoRepositorioFalhar() {
        String urlOriginal = "http://sub.dominio/path";
        String mensagemExcecao = "erro ao salvar no repositório";

        doThrow(new RuntimeException(mensagemExcecao)).when(linkRepository).save(any(Link.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> linkService.criaLink(urlOriginal));

        assertThat(ex.getMessage(), is(mensagemExcecao));
        verify(linkRepository, times(1)).save(any(Link.class));
        verify(validaURLRule, times(1)).validar(urlOriginal);
        verify(qrCodeService, times(1)).gerarQRCodeBase64(urlOriginal, 200, 200);
    }

    @Test
    void naoDeveCriarLinkQuandoGeracaoQRCodeFalhar() {
        String urlOriginal = "http://sub.dominio/path";
        String mensagemException = "nao foi possivel gerar o QR Code";
        int largura = 200, altura = 200;

        doThrow(new GeracaoQRCodeException(mensagemException, null))
                .when(qrCodeService)
                .gerarQRCodeBase64(urlOriginal, largura, altura);

        GeracaoQRCodeException ex = assertThrows(GeracaoQRCodeException.class, () -> linkService.criaLink(urlOriginal));

        assertThat(mensagemException, is(ex.toProblemDetail().getDetail()));

        verify(validaURLRule, times(1)).validar(urlOriginal);
        verify(linkRepository, never()).save(any(Link.class));
    }

    @Test
    void deveLancarExcecaoQuandoURLExpirar() {
        String urlEncurtada = "abc123";

        final int minutosExpirado = 3;
        var tempoExpirado = LocalDateTime.now().withMinute(minutosExpirado);
        String mensagemException = "O Link que você tentou acessar expirou.";
        Link link = Link.builder()
                .comUrlOriginal("http://site")
                .comUrlEncurtada(urlEncurtada)
                .comCriadaEm(tempoExpirado)
                .build();

        when(linkRepository.findByUrlEncurtada(urlEncurtada)).thenReturn(Optional.of(link));

        var ex = assertThrows(LinkExpiradoException.class, () -> linkService.getLinkPorUrlEncurtada(urlEncurtada));

        assertThat(mensagemException, is(ex.toProblemDetail().getDetail()));
        assertThat(HttpStatus.GONE.value(), equalTo(ex.toProblemDetail().getStatus()));

        verify(linkRepository, atLeastOnce()).findByUrlEncurtada(urlEncurtada);
        verify(linkRepository, atLeastOnce()).delete(link);
    }

    @Test
    void deveRedirecionarQuandoLinkNaoEstaExpirado() {
        String urlEncurtada = "abc123";

        var tempoNaoExpirado = LocalDateTime.now();

        Link link = Link.builder()
                .comUrlOriginal("http://site")
                .comUrlEncurtada(urlEncurtada)
                .comCriadaEm(tempoNaoExpirado)
                .build();

        when(linkRepository.findByUrlEncurtada(urlEncurtada)).thenReturn(Optional.of(link));

        assertDoesNotThrow(() -> {
            LinkResponse response = linkService.getLinkPorUrlEncurtada(urlEncurtada);

            assertThat(response, notNullValue());
        });

        verify(linkRepository, atLeastOnce()).findByUrlEncurtada(urlEncurtada);
        verify(linkRepository, never()).delete(any(Link.class));
    }
}