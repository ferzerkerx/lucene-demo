package com.ferzerkerx.lucenedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static com.ferzerkerx.lucenedemo.utils.OptionalUtils.getOrThrow;

@JsonIgnoreProperties
@JsonDeserialize(builder = Book.Builder.class)
@Immutable
public class Book {

    private final String title;

    private final String author;

    private Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @JsonPOJOBuilder(buildMethodName = "createBook", withPrefix = "with")
    public static class Builder {

        private Optional<String> title;
        private Optional<String> author;

        public Builder withTitle(String name) {
            this.title = Optional.of(name);
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = Optional.of(author);
            return this;
        }

        public Book createBook() {
            String bookTitle = getOrThrow(title, "Name");
            String bookAuthor = getOrThrow(author, "Author");
            return new Book(bookTitle, bookAuthor);
        }
    }
}
