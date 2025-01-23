package com.desafios.encurtador_url.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class LinkUtils {

    public static String geraAleatoriosAlfanumericos() {
        Random source = new Random();
        return RandomStringUtils.random(5, 0, 0, true, true, null, source);
    }
}