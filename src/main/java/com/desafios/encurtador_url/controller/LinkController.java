package com.desafios.encurtador_url.controller;

import com.desafios.encurtador_url.dto.LinkRequest;
import com.desafios.encurtador_url.dto.LinkResponse;
import com.desafios.encurtador_url.service.LinkService;
import com.desafios.encurtador_url.service.QRCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class LinkController {

    private final LinkService linkService;
    private final QRCodeService qrCodeService;

    public LinkController(LinkService linkService, QRCodeService qrCodeService) {
        this.linkService = linkService;
        this.qrCodeService = qrCodeService;
    }

    @PostMapping
    public ResponseEntity<LinkResponse> criaLink(@RequestBody LinkRequest request) {
        String urlOriginal = request.url();
        LinkResponse response = linkService.criaLink(urlOriginal);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{parteAleatoria}")
    public ResponseEntity<Void> redirecionaParaUrlOriginal(@PathVariable String parteAleatoria) {
        LinkResponse linkResponse = linkService.getLinkPorUrlEncurtada(parteAleatoria);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(linkResponse.urlOriginal()))
                .build();
    }

    @GetMapping(path = "/{parteAleatoria}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(@PathVariable String parteAleatoria) {
        LinkResponse linkResponse = linkService.getLinkPorUrlEncurtada(parteAleatoria);

        byte[] qrCode = qrCodeService.gerarQRCode(linkResponse.urlOriginal(), 200,200);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(qrCode);
    }
}
