package com.ferzerkerx.lucenedemo.utils;

import java.util.Optional;

public class OptionalUtils {

    private OptionalUtils() {
    }

    public static String getOrThrow(Optional<String> optional, String name) {
        return optional.orElseThrow(() -> new IllegalArgumentException(name + " is needed."));
    }
}
