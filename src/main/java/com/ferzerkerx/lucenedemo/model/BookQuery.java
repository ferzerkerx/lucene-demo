package com.ferzerkerx.lucenedemo.model;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@Immutable
public class BookQuery {


    private final String name;


    private BookQuery(String name) {
        this.name = name;
    }


    public static Builder builder() {
        return new Builder();
    }


    public String getName() {
        return name;
    }

    public static class Builder {


        private Optional<String> name = Optional.empty();

        public Builder withName(String name) {
            this.name = Optional.of(name);
            return this;
        }


        public BookQuery build() {
            String bookName = name.orElseThrow(() -> new IllegalArgumentException("name is required"));
            return new BookQuery(bookName);
        }

    }
}
