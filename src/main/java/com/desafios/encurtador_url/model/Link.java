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
    private String qrcode;

    private Link() {
    }

    public static LinkBuilder builder() {
        return new LinkBuilder();
    }

    public static class LinkBuilder {
        private final Link link;

        public LinkBuilder() {
            link = new Link();
        }

        public LinkBuilder comUrlOriginal(String urlOriginal) {
            link.urlOriginal = urlOriginal;
            return this;
        }

        public LinkBuilder comUrlEncurtada(String urlEncurtada) {
            link.urlEncurtada = urlEncurtada;
            return this;
        }

        public LinkBuilder comQrcode(String qrcode) {
            link.qrcode = qrcode;
            return this;
        }

        public LinkBuilder comCriadaEm(LocalDateTime criadaEm) {
            link.criadaEm = criadaEm;
            return this;
        }

        public Link build() {
            return link;
        }
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

    public String getQrcode() {
        return qrcode;
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

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public LinkResponse toLinkResponse() {
        return new LinkResponse(
                this.urlOriginal,
                this.urlEncurtada,
                this.criadaEm,
                this.qrcode
        );
    }
}
