package com.desafios.url_encurtada;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlEncurtadaApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UrlEncurtadaApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
