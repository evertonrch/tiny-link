package com.desafios.encurtador_url.service;

import com.desafios.encurtador_url.dto.LinkResponse;
import com.desafios.encurtador_url.exception.LinkNaoEncontradoException;
import com.desafios.encurtador_url.repository.LinkRepository;
import com.desafios.encurtador_url.rule.ValidaURLRule;
import com.desafios.encurtador_url.utils.LinkUtils;
import com.desafios.encurtador_url.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LinkService {

    private final Logger log = LoggerFactory.getLogger(LinkService.class);

    private final LinkRepository linkRepository;
    private final QRCodeService qrCodeService;
    private final ValidaURLRule validaURLRule;

    public LinkService(LinkRepository linkRepository, QRCodeService qrCodeService, ValidaURLRule validaURLRule) {
        this.linkRepository = linkRepository;
        this.qrCodeService = qrCodeService;
        this.validaURLRule = validaURLRule;
    }

    @Transactional
    public LinkResponse criaLink(String urlOriginal) {
        validaURLRule.validar(urlOriginal);
        Link link = getLink(urlOriginal);
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

    private Link getLink(String urlOriginal) {
        Link link = Link.comUrlOriginal(urlOriginal);
        link.setCriadaEm(LocalDateTime.now());
        link.setUrlEncurtada(LinkUtils.geraAleatoriosAlfanumericos());
        link.setQrcode(qrCodeService.gerarQRCodeBase64(urlOriginal, 200, 200));
        return link;
    }
}
