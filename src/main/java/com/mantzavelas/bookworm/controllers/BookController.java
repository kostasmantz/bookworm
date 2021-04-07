package com.mantzavelas.bookworm.controllers;

import com.mantzavelas.bookworm.resources.VisibleBookResource;
import com.mantzavelas.bookworm.resources.BookResource;
import com.mantzavelas.bookworm.services.BookService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResource createBook(@Valid @RequestBody BookResource resource) {
        return bookService.createNewBook(resource);
    }

    @PutMapping("/{bookId}")
    public BookResource updateBook(@PathVariable Long bookId, @Valid @RequestBody BookResource resource) {
        return bookService.createOrUpdateBook(bookId, resource);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/visible")
    public List<VisibleBookResource> getAllVisibleBooks() {
        return bookService.findAllVisible();
    }
}
