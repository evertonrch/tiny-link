package com.desafios.url_encurtada.controller;

import com.desafios.url_encurtada.dto.LinkResponse;
import com.desafios.url_encurtada.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping
    public ResponseEntity<?> criaLink(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String urlOriginal = body.get("url");
        LinkResponse response = linkService.criaLink(urlOriginal);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{parteAleatoria}")
    public ResponseEntity<Void> redirecionaParaUrlOriginal(@PathVariable String parteAleatoria, HttpServletResponse response) {
        LinkResponse linkResponse = linkService.getLinkPorUrlEncurtada(parteAleatoria);

        try {
            response.sendRedirect(linkResponse.urlOriginal());
            return ResponseEntity.ok().build();
        } catch (IOException ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
