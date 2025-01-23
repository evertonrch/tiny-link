package com.desafios.encurtador_url.model;

import com.desafios.encurtador_url.dto.LinkResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "links")
public class Link {

    @Id
    private String id;
    private String urlOriginal;
    private String urlEncurtada;
    private LocalDateTime criadaEm;

    private Link() {
    }

    public static Link comUrlOriginal(String urlOriginal) {
        Link link = new Link();
        link.setUrlOriginal(urlOriginal);
        link.setCriadaEm(LocalDateTime.now());
        return link;
    }

    public String getId() {
        return id;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public String getUrlEncurtada() {
        return urlEncurtada;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public void setUrlEncurtada(String urlEncurtada) {
        this.urlEncurtada = urlEncurtada;
    }

    public void setCriadaEm(LocalDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }

    public LinkResponse toLinkResponse() {
        return new LinkResponse(
                this.urlOriginal,
                this.urlEncurtada,
                this.criadaEm,
                true
        );
    }
}
