package com.ferzerkerx.lucenedemo.controller;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.List;

@RestController
public class BookController {

    @Nonnull
    private final BookService bookService;

    @Autowired
    public BookController(@Nonnull BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    @ResponseBody
    public List<Book> findBookByKeyword(@RequestParam(name = "keyword") String keyword) {
        return bookService.findBookByKeyword(keyword);
    }

}
