package com.ferzerkerx.lucenedemo.utils;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtils {

    private FileUtils() {

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
        } catch (URISyntaxException ignored) {
            throw new UncheckedIOException(new FileNotFoundException("Could not find file:"  + fileName));
        }
    }
}
