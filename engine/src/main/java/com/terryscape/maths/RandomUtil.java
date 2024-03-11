package com.terryscape.maths;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    private RandomUtil() {
    }

    public static int randomNumber(int lowerBoundInclusive, int inclusiveUpperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBoundInclusive, inclusiveUpperBound + 1);
    }

    public static <T> T randomCollection(T[] collection) {
        var random = RandomUtil.randomNumber(0, collection.length - 1);
        return collection[random];
    }

    public static <T> T randomCollection(List<T> collection) {
        var random = RandomUtil.randomNumber(0, collection.size() - 1);
        return collection.get(random);
    }

    public static boolean randomBool() {
        return randomNumber(0, 1) == 0;
    }

    public static float random01() {
        return ThreadLocalRandom.current().nextFloat();
    }
}
