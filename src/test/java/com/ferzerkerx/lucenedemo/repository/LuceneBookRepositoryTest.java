package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import com.ferzerkerx.lucenedemo.utils.LuceneBookIndexUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.resolvePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LuceneBookRepositoryTest {

    private static Directory DIRECTORY = new RAMDirectory();

    @Test
    void findByAuthor() {
        BookQuery bookQuery = BookQuery.builder()
                .withAuthorName("George")
                .build();

        assertWithQuery(bookQuery);
    }

    @Test
    void findByTitle() {
        BookQuery bookQuery = BookQuery.builder()
                .withTitle("1984")
                .build();

        assertWithQuery(bookQuery);
    }

    @Test
    void findByTitleAndAuthor() {
        BookQuery bookQuery = BookQuery.builder()
                .withTitle("1984")
                .withAuthorName("George")
                .build();

        assertWithQuery(bookQuery);
    }

    private void assertWithQuery(BookQuery bookQuery) {
        LuceneBookRepository luceneBookRepository = givenInstance();
        try (Stream<Book> books = luceneBookRepository.findBy(bookQuery)) {
            List<Book> foundBooks = books.collect(Collectors.toList());
            assertNotNull(foundBooks);
            assertEquals(1, foundBooks.size());

            Book book = foundBooks.get(0);
            assertEquals("1984", book.getTitle());
            assertEquals("George Orwell", book.getAuthor());
        }
    }

    private LuceneBookRepository givenInstance()  {
        try {
            return new LuceneBookRepository(DIRECTORY);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @BeforeAll
    private static void createIndex() {
        CsvBookSupplier csvBookSupplier = new CsvBookSupplier(resolvePath(LuceneBookRepositoryTest.class, "/books-test.csv"));
        LuceneBookIndexUtils.createIndex(DIRECTORY, csvBookSupplier);
    }
}