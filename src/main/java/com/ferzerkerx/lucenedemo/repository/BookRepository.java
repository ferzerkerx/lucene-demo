package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;

import java.util.stream.Stream;

public interface BookRepository {

    Stream<Book> findBy(BookQuery bookQuery);
}
