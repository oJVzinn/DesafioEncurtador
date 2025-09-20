package com.github.ojvzinn.desafioencurtador.utils;

import com.github.ojvzinn.desafioencurtador.enums.RandomType;

import java.util.Random;

public class RandomUtils {

    public static String generateRandom() {
        int randomNumber = new Random().nextInt(3);
        RandomType type = randomNumber == 0 ? RandomType.UPPERCASE : randomNumber == 1 ? RandomType.LOWERCASE : RandomType.NUMBER;
        return switch (type) {
            case UPPERCASE -> String.valueOf((char) (new Random().nextInt(26) + 'A'));
            case LOWERCASE -> String.valueOf((char) (new Random().nextInt(26) + 'a'));
            case NUMBER -> String.valueOf(new Random().nextInt(10));
        };
    }

}
