package com.iqb.consumer.common.utils;

import java.util.Random;

public class RandomUtils {

    public static String randomInt(int max) {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        for (int i = 0; i < max; i++) {
            sb.append(r.nextInt(10));
        }
        return sb.toString();
    }
}
