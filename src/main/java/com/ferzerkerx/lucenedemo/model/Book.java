package com.ferzerkerx.lucenedemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@JsonIgnoreProperties
@JsonDeserialize(builder = Book.Builder.class)
@Immutable
public class Book {


    private final String name;


    private Book(String name) {
        this.name = name;
    }


    public static Builder builder() {
        return new Builder();
    }


    public String getName() {
        return name;
    }

    @JsonPOJOBuilder(buildMethodName = "createBook", withPrefix = "with")
    public static class Builder {


        private Optional<String> name;


        public Builder withName(String name) {
            this.name = Optional.of(name);
            return this;
        }


        public Book createBook() {
            return new Book(name.orElseThrow(() -> new IllegalArgumentException("Name is needed.")));
        }
    }
}
