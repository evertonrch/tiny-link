package com.desafios.encurtador_url.service;

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

@Service
public class QRCodeService {

    private final Logger log = LoggerFactory.getLogger(QRCodeWriter.class);

    public byte[] gerarQrcode(String url, int largura, int altura) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, largura, altura);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream, alteraCor());

            return outputStream.toByteArray();
        } catch (WriterException | IOException ex) {
            log.error("não foi possível gerar o QRCode. {}", ex.toString());
            throw new RuntimeException(ex);
        }
    }

    private MatrixToImageConfig alteraCor() {
        // azul
        int onColor = 0xFF1E90FF;
        // branco
        int offColor = 0xFFFFFFFF;

        return new MatrixToImageConfig(onColor, offColor);
    }
}
