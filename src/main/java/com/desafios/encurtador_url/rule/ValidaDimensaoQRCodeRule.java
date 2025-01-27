package com.desafios.encurtador_url.rule;

import com.desafios.encurtador_url.exception.DimensaoQRCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidaDimensaoQRCodeRule {

    private final Logger log = LoggerFactory.getLogger(ValidaDimensaoQRCodeRule.class);

    public void validar(int altura, int largura) {
        if(largura <= 0 || altura <= 9) {
            log.error("erro ao criar dimensões com altura {} e largura {}", altura, largura);
            throw new DimensaoQRCodeException("As dimensões passadas para o QR Code estão inválidas");
        }
    }
}
