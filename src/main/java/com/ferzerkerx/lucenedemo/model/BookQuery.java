package com.ferzerkerx.lucenedemo.model;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Immutable
public final class BookQuery {

    @Nullable
    private final String author;

    @Nullable
    private final String title;

    private BookQuery(@Nullable String author, @Nullable String title) {
        this.author = author;
        this.title = title;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public static class Builder {

        private String title;
        private String author;

        public Builder withTitle(String title) {
            requireNonNull(title);
            this.title = title;
            return this;
        }

        public Builder withAuthorName(String authorName) {
            requireNonNull(authorName);
            this.author = authorName;
            return this;
        }

        public BookQuery build() {
            return new BookQuery(author, title);
        }
    }
}
