package com.ferzerkerx.lucenedemo.service;

import com.ferzerkerx.lucenedemo.model.Book;

import java.util.List;

public interface BookService {


    List<Book> findBookByKeyword(String name);
}
