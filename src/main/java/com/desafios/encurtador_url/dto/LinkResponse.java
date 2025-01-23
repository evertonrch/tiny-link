package com.desafios.encurtador_url.dto;

import java.time.LocalDateTime;

public record LinkResponse(
        String urlOriginal,
        String urlEncurtada,
        LocalDateTime criadaEm,
        boolean estaAtiva
) {}
