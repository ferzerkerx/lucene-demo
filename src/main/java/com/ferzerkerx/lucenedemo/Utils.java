package com.ferzerkerx.lucenedemo;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

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
}
