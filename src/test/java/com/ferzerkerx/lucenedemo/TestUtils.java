package com.ferzerkerx.lucenedemo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public final class TestUtils {

    private TestUtils() {
        throw new AssertionError();
    }

    public static String resource(String resourceName) {
        requireNonNull(resourceName);
        try {
            URL resource = TestUtils.class.getResource("/" + resourceName);
            return new String(Files.readAllBytes(Paths.get(resource.getPath())));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
