package com.ferzerkerx.lucenedemo.model;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@Immutable
public class BookQuery {

    @Nonnull
    private final String name;

    @Nonnull
    private BookQuery(@Nonnull String name) {
        this.name = name;
    }

    @Nonnull
    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public static class Builder {

        @Nonnull
        private Optional<String> name = Optional.empty();

        public Builder withName(@Nonnull String name) {
            this.name = Optional.of(name);
            return this;
        }

        @Nonnull
        public BookQuery build() {
            String bookName = name.orElseThrow(() -> new IllegalArgumentException("name is required"));
            return new BookQuery(bookName);
        }

    }
}
