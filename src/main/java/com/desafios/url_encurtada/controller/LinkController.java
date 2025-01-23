package com.desafios.url_encurtada.controller;

import com.desafios.url_encurtada.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import model.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        Link link = linkService.criaLink(urlOriginal, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(link.toLinkResponse());
    }
}
