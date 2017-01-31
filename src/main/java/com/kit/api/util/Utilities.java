package com.kit.api.util;


import com.kit.api.collection.StatePredicate;
import com.kit.api.collection.StatePredicate;

import java.util.Random;

import static java.lang.Thread.currentThread;

/**
 * Author: tobiewarburton
 */
public class Utilities {
    private static final Random random = new Random();

    private Utilities() {
    }

    public static int random(int min, int max) {
        if (min < 0 || max < 0 || Math.abs(max - min) <= 0) {
            return min;
        }
        return random.nextInt(Math.abs(max - min)) + min;
    }

    public static double random(double min, double max) {
        return (min + (Math.random() * max));
    }


    public static long random(long min, long max) {
        return (min + ((long) (Math.random() * max)));
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted");
        }
    }

    public static void sleep(int min, int max) {
        int ms = random(min, max);
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep was interrupted");
        }
    }

    /**
     * Sleeps until the passed predicate returns true or sleeping for longer than the timeout.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepUntil(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = !predicate.apply();
        while (success && !currentThread().isInterrupted() && timer.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = !predicate.apply();
        }
        return !success;
    }

    /**
     * Sleeps until the passed predicate returns false.
     *
     * @param predicate predicate
     * @param timeOut   millis time out
     */
    public static boolean sleepWhile(StatePredicate predicate, long timeOut) {
        Timer timer = new Timer(timeOut);
        boolean success = predicate.apply();
        while (success && !currentThread().isInterrupted() && timer.isRunning()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Sleep was interrupted");
            }
            success = predicate.apply();
        }
        return !success;
    }



    private static char[] c = new char[]{'K', 'M', 'B', 'T'};

    public static String simpleFormat(double val, int iteration) {
        if (val < 1000) {
            return (int) val + "GP";
        }
        double d = (val / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : simpleFormat(d, iteration+1));

    }

    public static String prettyFormat(long val) {
        String s = String.valueOf(val);
        if (val >= 1000000000) {
            return s.charAt(0) + "," + s.substring(1, 4) + "," + s.substring(4, 7) + "," + s.substring(7);
        } else if (val >= 100000000) {
            return s.substring(0, 3) + "," + s.substring(3, 6) + "," + s.substring(6);
        } else if (val >= 10000000) {
            return s.substring(0, 2) + "," + s.substring(2, 5) + "," + s.substring(5);
        } else if (val >= 1000000) {
            return s.charAt(0) + "," + s.substring(1, 4) + "," + s.substring(4);
        } else if (val >= 100000) {
            return s.substring(0, 3) + "," + s.substring(3);
        } else if (val >= 10000) {
            return s.substring(0, 2) + "," + s.substring(2);
        } else if (val >= 1000) {
            return s.charAt(0) + "," + s.substring(1);
        }
        return s;
    }
}
