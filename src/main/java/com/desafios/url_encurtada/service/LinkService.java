package com.desafios.url_encurtada.service;

import com.desafios.url_encurtada.dto.LinkResponse;
import com.desafios.url_encurtada.repository.LinkRepository;
import com.desafios.url_encurtada.utils.LinkUtils;
import jakarta.servlet.http.HttpServletRequest;
import model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LinkService {

    private final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public LinkResponse criaLink(String urlOriginal, HttpServletRequest request) {
        Link link = Link.comUrlOriginal(urlOriginal);
        link.setCriadaEm(LocalDateTime.now());
        link.setUrlEncurtada(geraUrlEncurtada(request));

        Link newLink = linkRepository.save(link);
        log.info("url encurtada {} com id {} criada.", link.getUrlEncurtada(), newLink.getId());

        return newLink.toLinkResponse();
    }

    private String geraUrlEncurtada(HttpServletRequest request) {
        return request.getRequestURL()
                .append(LinkUtils.geraAleatoriosAlfanumericos())
                .toString();
    }
}
