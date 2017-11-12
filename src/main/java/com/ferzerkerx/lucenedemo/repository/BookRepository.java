package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public interface BookRepository {

    @Nonnull
    Stream<? extends Book> findBy(@Nonnull BookQuery bookQuery);
}
