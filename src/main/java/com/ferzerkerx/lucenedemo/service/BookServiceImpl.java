package com.ferzerkerx.lucenedemo.service;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import com.ferzerkerx.lucenedemo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookServiceImpl implements BookService {

    @Nonnull
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(@Nonnull BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Nonnull
    @Override
    public List<Book> findBookByKeyword(@Nonnull String name) {
        BookQuery bookQuery = BookQuery.builder()
                .withName(name)
                .build();

        try (Stream<? extends Book> books = bookRepository.findBy(bookQuery)) {
            return books.collect(Collectors.toList());
        }
    }
}
