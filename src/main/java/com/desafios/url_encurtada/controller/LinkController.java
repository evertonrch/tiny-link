package com.desafios.url_encurtada.controller;

import com.desafios.url_encurtada.dto.LinkResponse;
import com.desafios.url_encurtada.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<LinkResponse> criaLink(@RequestBody Map<String, String> body) {
        String urlOriginal = body.get("url");
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
}
