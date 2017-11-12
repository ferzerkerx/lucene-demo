package com.ferzerkerx.lucenedemo.service;

import com.ferzerkerx.lucenedemo.model.Book;

import javax.annotation.Nonnull;
import java.util.List;

public interface BookService {

    @Nonnull
    List<Book> findBookByKeyword(@Nonnull String name);
}
