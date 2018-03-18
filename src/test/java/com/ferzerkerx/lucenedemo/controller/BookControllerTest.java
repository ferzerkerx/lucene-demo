package com.ferzerkerx.lucenedemo.controller;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.ferzerkerx.lucenedemo.TestUtils.resource;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void findBookByKeyword() throws Exception {
        doReturn(singletonList(book()))
            .when(bookService).findBookByKeyword("someName");

        this.mockMvc.perform(
                get("/books")
                        .param("keyword", "someName"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(resource("books-response.json"), true));
    }

    private static Book book() {
        return Book.builder()
                .withAuthor("author")
                .withTitle("title")
                .createBook();
    }
}