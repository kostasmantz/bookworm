package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.commons.JsonUtil;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private static final String BOOK_TITLE = "Dummy book";
    private static final String BOOK_ISBN = "9780684800011";
    private static final String ENDPOINT_API_BOOKS = "/api/books";
    public static final String ENDPOINT_BOOKS_WITH_BOOK_ID = "/api/books/1";

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

    @Test
    void testUpdateBookWithMissingTitle_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .status(BookStatus.LIVE)
                .isbn(BOOK_ISBN)
                .build();
        try {
            mockMvc.perform(put(ENDPOINT_BOOKS_WITH_BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testUpdateBookWithMissingStatus_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .isbn(BOOK_ISBN)
                .build();
        try {
            mockMvc.perform(put(ENDPOINT_BOOKS_WITH_BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testUpdateBookWithMissingIsbn_ShouldReturn400() {
        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .status(BookStatus.EDITING)
                .build();
        try {
            mockMvc.perform(put(ENDPOINT_BOOKS_WITH_BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testUpdateBookWithAllRequiredFields_ShouldReturn200() {
        BookResource resource = BookResource.builder()
            .title(BOOK_TITLE)
            .status(BookStatus.EDITING)
            .isbn(BOOK_ISBN)
            .build();
        try {
            mockMvc.perform(put(ENDPOINT_BOOKS_WITH_BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testDeleteBookWithValidIdParam_ShouldReturn200() {
        try {
            mockMvc.perform(delete(ENDPOINT_BOOKS_WITH_BOOK_ID)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        verify(bookService).deleteBook(1L);
    }

    @Test
    void testGetAllVisibleBooks_ShouldReturn200() {
        try {
            mockMvc.perform(get("/api/books/visible")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        verify(bookService).findAllVisible();
    }

    @Test
    void testGetBookDetails_ShouldReturn404() {
        when(bookService.getDetailsForBook(any())).thenThrow(ResourceNotFoundException.class);
        try {
            mockMvc.perform(get("/api/books/1/details")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }

        verify(bookService).getDetailsForBook(any());
    }

    @Test
    void testGetBookDetails_ShouldReturn200() {
        try {
            mockMvc.perform(get("/api/books/1/details")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        verify(bookService).getDetailsForBook(any());
    }

}