package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.resources.PublisherResource;
import com.mantzavelas.bookworm.services.PublisherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public PublisherResource createPublisher(@Valid @RequestBody PublisherResource resource) {
        return publisherService.createNewPublisher(resource);
    }
}
