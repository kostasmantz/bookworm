package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.resources.AuthorResource;
import com.mantzavelas.bookworm.resources.BookResource;
import com.mantzavelas.bookworm.services.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/{authorId}/books")
    public List<BookResource> getAuthorBooks(@PathVariable Long authorId) {
        return authorService.getBooks(authorId);
    }
}
