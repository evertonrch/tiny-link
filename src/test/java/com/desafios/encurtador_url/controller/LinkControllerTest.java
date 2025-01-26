package com.desafios.encurtador_url.controller;

import com.desafios.encurtador_url.dto.LinkRequest;
import com.desafios.encurtador_url.dto.LinkResponse;
import com.desafios.encurtador_url.exception.LinkNaoEncontradoException;
import com.desafios.encurtador_url.service.LinkService;
import com.desafios.encurtador_url.service.QRCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LinkControllerTest {

    @InjectMocks
    private LinkController linkController;
    @Mock
    private LinkService linkService;
    @Mock
    private QRCodeService qrCodeService;

    @Test
    void deveRetornar201AoCriarLinkComSucesso() {
        LinkRequest request = new LinkRequest("https://example.com");
        String urlOriginal = request.url();

        LinkResponse response = new LinkResponse(
                urlOriginal,
                "dfg45",
                null,
                null
        );

        when(linkService.criaLink(urlOriginal)).thenReturn(response);

        ResponseEntity<LinkResponse> responseEntity = linkController.criaLink(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(), "O status HTTP deveria ser 201");
        assertThat(response, sameInstance(responseEntity.getBody()));

        verify(linkService, times(1)).criaLink(urlOriginal);
    }

    @Test
    void deveLancaExcecaoQuandoServicoFalha() {
        LinkRequest request = new LinkRequest("https://example.com");
        String urlOriginal = request.url();
        String mensagemExcecao = "Erro no serviço";

        when(linkService.criaLink(urlOriginal)).thenThrow(new RuntimeException(mensagemExcecao));

        Exception ex = assertThrows(Exception.class, () -> linkController.criaLink(request));

        assertEquals(mensagemExcecao, ex.getMessage());

        verify(linkService, times(1)).criaLink(urlOriginal);
    }

    @Test
    void deveRedirecionarQuandoAURLEncurtadaEValida() {
        String urlEncurtada = "sdf23";
        String urlOriginal = "http://example.com";

        LinkResponse response = new LinkResponse(
                urlOriginal,
                urlEncurtada,
                null,
                null
        );

        when(linkService.getLinkPorUrlEncurtada(urlEncurtada)).thenReturn(response);

        ResponseEntity<Void> responseEntity = linkController.redirecionaParaUrlOriginal(urlEncurtada);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode(), "O status HTTP deve ser 302");

        assertThat(responseEntity.getHeaders().getLocation(), is(notNullValue()));
        assertThat(urlOriginal, equalTo(responseEntity.getHeaders().getLocation().toString()));

        verify(linkService, times(1)).getLinkPorUrlEncurtada(urlEncurtada);
    }

    @Test
    void deveRetornar404QuandoURLEncurtadaNaoExiste() {
        String urlEncurtada = "invalida";
        String mensagemException = "Não foi possível encontrar o link.";

        when(linkService.getLinkPorUrlEncurtada(urlEncurtada)).thenThrow(new LinkNaoEncontradoException(mensagemException));

        LinkNaoEncontradoException ex = assertThrows(LinkNaoEncontradoException.class, () -> linkController.redirecionaParaUrlOriginal(urlEncurtada));

        assertThat(mensagemException, is(equalTo(ex.toProblemDetail().getDetail())));
        assertThat(HttpStatus.NOT_FOUND.value(), is(ex.toProblemDetail().getStatus()));

        verify(linkService, atLeastOnce()).getLinkPorUrlEncurtada(urlEncurtada);
    }

    @Test
    void deveRetornarQRCodeParaUrlEncurtada() {
        // Cenário: Definir URL encurtada e QR Code mockado
        String urlEncurtada = "abc123";
        String urlOriginal = "https://example.com";
        byte[] qrcodeBytes = new byte[]{1, 2, 3, 4, 5};

        LinkResponse linkResponse = new LinkResponse(
                urlOriginal,
                urlEncurtada,
                null,
                null
        );

        when(linkService.getLinkPorUrlEncurtada(urlEncurtada)).thenReturn(linkResponse);
        when(qrCodeService.gerarQRCode(urlOriginal, 200, 200)).thenReturn(qrcodeBytes);

        ResponseEntity<byte[]> responseEntity = linkController.getQRCode(urlEncurtada);

        assertThat(HttpStatus.OK, is(equalTo(responseEntity.getStatusCode())));
        assertThat(MediaType.IMAGE_PNG, is(equalTo(responseEntity.getHeaders().getContentType())));
        assertThat(qrcodeBytes, is(equalTo(responseEntity.getBody())));

        verify(linkService, times(1)).getLinkPorUrlEncurtada(urlEncurtada);
        verify(qrCodeService, times(1)).gerarQRCode(urlOriginal, 200, 200);
    }

    @Test
    void deveRetornar404QuandoUrlEncurtadaNaoExiste() {
        String urlEncurtada = "invalida";
        String mensagemException = "Url não encontrada";

        when(linkService.getLinkPorUrlEncurtada(urlEncurtada)).thenThrow(new LinkNaoEncontradoException(mensagemException));

        LinkNaoEncontradoException ex = assertThrows(LinkNaoEncontradoException.class, () -> linkController.redirecionaParaUrlOriginal(urlEncurtada));

        assertThat("A mensagem da exceção deve ser igual ao detalhe do problema", mensagemException, containsString(ex.toProblemDetail().getDetail()));
        assertThat("O status HTTP deve ser NOT_FOUND", HttpStatus.NOT_FOUND.value(), is(ex.toProblemDetail().getStatus()));

        verify(linkService, times(1)).getLinkPorUrlEncurtada(urlEncurtada);
        verifyNoInteractions(qrCodeService);
    }
}