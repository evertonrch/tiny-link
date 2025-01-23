package com.desafios.url_encurtada.dto;

import java.time.LocalDateTime;

public record LinkResponse(
        String urlOriginal,
        String urlEncurtada,
        LocalDateTime criadaEm,
        boolean estaAtiva
) {}
