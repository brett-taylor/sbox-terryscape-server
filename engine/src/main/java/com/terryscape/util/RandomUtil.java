package com.terryscape.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private RandomUtil() {
    }

    public static int randomNumber(int lowerBound, int inclusiveUpperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBound, inclusiveUpperBound + 1);
    }
}
