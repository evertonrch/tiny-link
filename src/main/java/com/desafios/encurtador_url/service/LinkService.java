package com.desafios.encurtador_url.service;

import com.desafios.encurtador_url.dto.LinkResponse;
import com.desafios.encurtador_url.exception.LinkNaoEncontradoException;
import com.desafios.encurtador_url.repository.LinkRepository;
import com.desafios.encurtador_url.utils.LinkUtils;
import model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LinkService {

    private final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Transactional
    public LinkResponse criaLink(String urlOriginal) {
        Link link = Link.comUrlOriginal(urlOriginal);
        link.setCriadaEm(LocalDateTime.now());
        link.setUrlEncurtada(LinkUtils.geraAleatoriosAlfanumericos());

        Link newLink = linkRepository.save(link);
        log.info("url encurtada {} com id {} criada.", link.getUrlEncurtada(), newLink.getId());

        return newLink.toLinkResponse();
    }

    public LinkResponse getLinkPorUrlEncurtada(String urlEncurtada) {
         return linkRepository.findByUrlEncurtada(urlEncurtada)
                 .orElseThrow(() -> {
                     log.error("erro ao buscar url encurtada: {}", urlEncurtada);
                     return new LinkNaoEncontradoException("Não foi possível encontrar o link.");
                 })
                 .toLinkResponse();
    }
}
