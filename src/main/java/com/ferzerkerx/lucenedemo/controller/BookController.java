package com.ferzerkerx.lucenedemo.controller;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @CrossOrigin
    @GetMapping(value = "/books")
    @ResponseBody
    public List<Book> findBookByKeyword(@RequestParam(name = "keyword") String keyword) {
        return bookService.findBookByKeyword(keyword);
    }

}
