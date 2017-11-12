package com.ferzerkerx.lucenedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@JsonIgnoreProperties
@JsonDeserialize(builder = Book.Builder.class)
@Immutable
public class Book {

    @Nonnull
    private final String name;

    @Nonnull
    private Book(@Nonnull String name) {
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

    @JsonPOJOBuilder(buildMethodName = "createBook", withPrefix = "with")
    public static class Builder {

        @Nonnull
        private Optional<String> name;

        @Nonnull
        public Builder withName(@Nonnull String name) {
            this.name = Optional.of(name);
            return this;
        }

        @Nonnull
        public Book createBook() {
            return new Book(name.orElseThrow(() -> new IllegalArgumentException("Name is needed.")));
        }
    }
}
