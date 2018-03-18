package com.ferzerkerx.lucenedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.annotation.concurrent.Immutable;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties
@JsonDeserialize(builder = Book.Builder.class)
@Immutable
public class Book {

    private final String title;

    private final String author;

    private Book(String title, String author) {
        requireNonNull(title);
        requireNonNull(author);
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

    @JsonPOJOBuilder(buildMethodName = "createBook")
    public static class Builder {

        private String title;
        private String author;

        public Builder withTitle(String name) {
            requireNonNull(name);
            this.title = name;
            return this;
        }

        public Builder withAuthor(String author) {
            requireNonNull(author);
            this.author = author;
            return this;
        }

        public Book createBook() {
            requireNonNull(author);
            requireNonNull(title);
            return new Book(title, author);
        }
    }
}
