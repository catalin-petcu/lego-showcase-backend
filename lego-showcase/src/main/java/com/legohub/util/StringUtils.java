package com.legohub.util;

public class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
