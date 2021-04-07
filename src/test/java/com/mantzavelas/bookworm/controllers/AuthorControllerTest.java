package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.commons.JsonUtil;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.resources.AuthorResource;
import com.mantzavelas.bookworm.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    public static final String AUTHOR_LASTNAME = "Doe";
    public static final String AUTHOR_EMAIL = "john.doe@gmail.com";
    public static final String AUTHOR_FIRSTNAME = "John";
    public static final String INVALID_EMAIL = "aninvalidemail";
    @Mock
    private AuthorService service;
    @InjectMocks
    private AuthorController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createAuthorWithMissingFirstName_ShouldReturn400() {
        AuthorResource resource = AuthorResource.builder()
            .lastName(AUTHOR_LASTNAME)
            .email(AUTHOR_EMAIL)
            .build();

        try {
            mockMvc.perform(post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        verifyNoInteractions(service);
    }

    @Test
    void createAuthorWithMissingLastName_ShouldReturn400() {
        AuthorResource resource = AuthorResource.builder()
            .firstName(AUTHOR_FIRSTNAME)
            .email(AUTHOR_EMAIL)
            .build();

        try {
            mockMvc.perform(post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        verifyNoInteractions(service);
    }

    @Test
    void createAuthorWithInvalidEmail_ShouldReturn400() {
        AuthorResource resource = AuthorResource.builder()
            .firstName(AUTHOR_FIRSTNAME)
            .lastName(AUTHOR_LASTNAME)
            .email(INVALID_EMAIL)
            .build();

        try {
            mockMvc.perform(post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        verifyNoInteractions(service);
    }

    @Test
    void createAuthorWithValidResource_ShouldReturn200() {
        AuthorResource resource = AuthorResource.builder()
            .firstName(AUTHOR_FIRSTNAME)
            .lastName(AUTHOR_LASTNAME)
            .email(AUTHOR_EMAIL)
            .build();

        try {
            mockMvc.perform(post("/api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        verify(service).createNewAuthor(any());
    }

    @Test
    void testGetAuthorBooksWithAuthorNotFound_ShouldReturn404() {
        when(service.getBooks(any())).thenThrow(ResourceNotFoundException.class);

        try {
            mockMvc.perform(get("/api/authors/1/books")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testGetAuthorBooks_ShouldReturn200() {
        when(service.getBooks(any())).thenReturn(Collections.emptyList());

        try {
            mockMvc.perform(get("/api/authors/1/books")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }
    }
}