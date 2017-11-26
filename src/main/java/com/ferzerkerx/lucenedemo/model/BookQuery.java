package com.ferzerkerx.lucenedemo.model;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static com.ferzerkerx.lucenedemo.Utils.getOrThrow;

@Immutable
public class BookQuery {

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

        private Optional<String> title = Optional.empty();
        private Optional<String> author = Optional.empty();

        public Builder withTitle(String title) {
            this.title = Optional.of(title);
            return this;
        }

        public Builder withAuthorName(String authorName) {
            this.author = Optional.of(authorName);
            return this;
        }

        public BookQuery build() {
            String bookTitle = getOrThrow(title, "Title");
            String authorName = getOrThrow(author, "Author");
            return new BookQuery(authorName, bookTitle);
        }
    }
}
