package com.ferzerkerx.lucenedemo.csv;

import com.ferzerkerx.lucenedemo.model.Book;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.resolvePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvBookSupplierTest {

    @Test
    void stream() {
        CsvBookSupplier csvBookSupplier = givenInstance();

        try (Stream<Book> stream = csvBookSupplier.stream()) {
            List<Book> books = stream.collect(Collectors.toList());
            assertNotNull(books);
            assertEquals(3, books.size());
            Book book = books.get(0);
            assertEquals("Harper Lee", book.getAuthor());
            assertEquals("To Kill a Mockingbird", book.getTitle());
        }
    }

    @Test
    void get() {
        CsvBookSupplier csvBookSupplier = givenInstance();
        Book book = csvBookSupplier.get();
        assertNotNull(book);
        assertEquals("Harper Lee", book.getAuthor());
        assertEquals("To Kill a Mockingbird", book.getTitle());
    }

    private CsvBookSupplier givenInstance() {
        return new CsvBookSupplier(resolvePath(CsvBookSupplierTest.class, "/books-test.csv"));
    }
}