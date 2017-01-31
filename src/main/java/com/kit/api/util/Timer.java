package com.kit.api.util;

/**
 * Author: const_
 */
public class Timer {

    private long start;
    private long end;

    public Timer(long runTime) {
        reset(runTime);
    }

    /**
     * Resets the timer with a new runtime.
     *
     * @param runTime The amount of time the timer should run for.
     */
    public void reset(long runTime) {
        if (runTime < 0) {
            throw new IllegalArgumentException("Can't have a Timer with a negative time!");
        }
        start = System.currentTimeMillis();
        end = System.currentTimeMillis() + runTime;
    }

    /**
     * Checks if the timer is still running
     *
     * @return <t>true if the run time has not yet been met</t> otherwise false
     */
    public boolean isRunning() {
        return System.currentTimeMillis() < end;
    }

    /**
     * Gets the amount of time remaining
     *
     * @return the amount of time remaining
     */
    public long remaining() {
        return end - System.currentTimeMillis();
    }

    /**
     * Gets the amount of time elapsed
     *
     * @return the amount of time elapsed
     */
    public long elapsed() {
        return System.currentTimeMillis() - start;
    }

    /**
     * Formats a long into a formatted time String
     *
     * @param timeMilliseconds the long to format
     * @return a string
     */
    public static String formatTime(long timeMilliseconds) {
        int seconds = ((int) ((timeMilliseconds / 1000) % 60));
        int mins = ((int) (((timeMilliseconds / 1000) / 60) % 60));
        int hours = ((int) ((((timeMilliseconds / 1000) / 60) / 60) % 60));
        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }

    public String formatTime() {
        return formatTime(elapsed());
    }

}
