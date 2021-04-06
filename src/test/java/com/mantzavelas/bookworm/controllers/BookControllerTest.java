package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.commons.JsonUtil;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.resources.BookResource;
import com.mantzavelas.bookworm.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String BOOK_TITLE = "Dummy book";
    private static final String BOOK_ISBN = "9780684800011";
    private static final String ENDPOINT_API_BOOKS = "/api/books";

    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testCreateBookWithMissingTitle_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .status(BookStatus.LIVE)
                .isbn(BOOK_ISBN)
                .build();
        try {
            mockMvc.perform(post(ENDPOINT_API_BOOKS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testCreateBookWithMissingStatus_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .isbn(BOOK_ISBN)
                .build();
        try {
            mockMvc.perform(post(ENDPOINT_API_BOOKS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testCreateBookWithMissingIsbn_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .status(BookStatus.LIVE)
                .build();
        try {
            mockMvc.perform(post(ENDPOINT_API_BOOKS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testCreateBookWithAllRequiredFields_ShouldReturn200() {
        BookResource returnedResource = BookResource.builder()
                .id(1L)
                .title(BOOK_TITLE)
                .status(BookStatus.LIVE)
                .isbn(BOOK_ISBN)
                .build();
        when(bookService.createNewBook(any())).thenReturn(returnedResource);

        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .status(BookStatus.LIVE)
                .isbn(BOOK_ISBN)
                .build();
        try {
            mockMvc.perform(post(ENDPOINT_API_BOOKS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(JsonUtil.toJsonString(returnedResource)));
        } catch (Exception e) {
            fail(e);
        }
    }
}