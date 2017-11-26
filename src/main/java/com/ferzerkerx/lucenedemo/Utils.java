package com.ferzerkerx.lucenedemo;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public final class Utils {

    private Utils() {

    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {

        }
    }

    public static Path resolvePath(Class<?> clazz, String fileName){
        URL resourceUrl = clazz.getResource(fileName);
        try {
            return Paths.get(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            throw new UncheckedIOException(new FileNotFoundException("Could not find file:"  + fileName));
        }
    }

    public static String getOrThrow(Optional<String> optional, String name) {
        return optional.orElseThrow(() -> new IllegalArgumentException(name + " is needed."));
    }
}
