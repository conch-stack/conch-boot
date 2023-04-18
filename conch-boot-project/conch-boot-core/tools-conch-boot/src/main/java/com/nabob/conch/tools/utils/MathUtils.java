package com.nabob.conch.tools.utils;

public class MathUtils {


    /**
     * return 3/4 * origin + 1/4 * fix
     */
    public static int fix(int origin, int fix) {
        return (origin >> 1) + (origin >> 2) + (fix >> 2);
    }

    /**
     * return 3/4 * origin + 1/4 * fix
     */
    public static long fix(long origin, long fix) {
        return (origin >> 1) + (origin >> 2) + (fix >> 2);
    }
}
