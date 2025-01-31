package com.desafios.encurtador_url.service;

import com.desafios.encurtador_url.exception.GeracaoQRCodeException;
import com.desafios.encurtador_url.rule.ValidaDimensaoQRCodeRule;
import com.desafios.encurtador_url.rule.ValidaURLRule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    private final Logger log = LoggerFactory.getLogger(QRCodeWriter.class);

    private final ValidaURLRule validaURLRule;
    private final ValidaDimensaoQRCodeRule validaDimensaoQRCodeRule;

    public QRCodeService(ValidaURLRule validaURLRule, ValidaDimensaoQRCodeRule validaDimensaoQRCodeRule) {
        this.validaURLRule = validaURLRule;
        this.validaDimensaoQRCodeRule = validaDimensaoQRCodeRule;
    }

    public byte[] gerarQRCode(String url, int largura, int altura) {
        validaURLRule.validar(url);
        validaDimensaoQRCodeRule.validar(altura, largura);

        try {
            BitMatrix bitMatrix = gerarBitMatrix(url, largura, altura);
            return escreverQrCodeEmBytes(bitMatrix);
        } catch (IOException | WriterException | IllegalArgumentException ex) {
            log.error("problema ao gerar QRCode com a url {}: {}", url, ex.toString());
            throw new GeracaoQRCodeException("Falha ao gerar o QRCode.", ex);
        }
    }

    public String gerarQRCodeBase64(String url, int largura, int altura) {
        validaURLRule.validar(url);
        validaDimensaoQRCodeRule.validar(altura, largura);

        byte[] qrcode = gerarQRCode(url, largura, altura);
        return Base64.getEncoder().encodeToString(qrcode);
    }

    private byte[] escreverQrCodeEmBytes(BitMatrix bitMatrix) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, alteraCor());
        return outputStream.toByteArray();
    }

    private BitMatrix gerarBitMatrix(String url, int largura, int altura) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        return writer.encode(url, BarcodeFormat.QR_CODE, largura, altura);
    }

    private MatrixToImageConfig alteraCor() {
        int azul = 0xFF1E90FF, branco = 0xFFFFFFFF;
        return new MatrixToImageConfig(azul, branco);
    }
}
