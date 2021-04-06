package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.resources.AuthorResource;
import com.mantzavelas.bookworm.services.AuthorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public AuthorResource createAuthor(@Valid @RequestBody AuthorResource resource) {
        return authorService.createNewAuthor(resource);
    }
}
