package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.commons.JsonUtil;
import com.mantzavelas.bookworm.resources.PublisherResource;
import com.mantzavelas.bookworm.services.PublisherService;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PublisherControllerTest {

    public static final String ENDPOINT_API_PUBLISHERS = "/api/publishers";
    public static final String PUBLISHER_NAME = "Dummy publisher";
    @Mock
    private PublisherService service;
    @InjectMocks
    private PublisherController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPublisherWithMissingName_ShouldReturn400() {
        PublisherResource resource = PublisherResource.builder().build();
        try {
            mockMvc.perform(post(ENDPOINT_API_PUBLISHERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                .andExpect(status().isBadRequest());
        } catch (Exception e) {
            fail(e);
        }

        verifyNoInteractions(service);
    }

    @Test
    void createPublisherWithRequiredFields_ShouldCreatePublisher() {
        PublisherResource resource = PublisherResource.builder()
            .name(PUBLISHER_NAME)
            .build();

        try {
            mockMvc.perform(post(ENDPOINT_API_PUBLISHERS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJsonString(resource)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            fail(e);
        }

        verify(service).createNewPublisher(any());
    }
}