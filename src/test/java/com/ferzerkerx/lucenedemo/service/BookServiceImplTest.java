package com.ferzerkerx.lucenedemo.service;

import com.ferzerkerx.lucenedemo.model.BookQuery;
import com.ferzerkerx.lucenedemo.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    void findBookByKeyword() {
        ArgumentCaptor<BookQuery> bookQueryArgumentCaptor = ArgumentCaptor.forClass(BookQuery.class);

        String name = "someName";
        bookService.findBookByKeyword(name);
        verify(bookRepository).findBy(bookQueryArgumentCaptor.capture());

        BookQuery capturedquery = bookQueryArgumentCaptor.getValue();
        assertNotNull(capturedquery);
        assertEquals(name, capturedquery.getTitle().orElse(""));
        assertEquals(name, capturedquery.getAuthor().orElse(""));
    }
}