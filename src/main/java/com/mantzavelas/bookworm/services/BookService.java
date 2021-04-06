package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.repositories.BookRepository;
import com.mantzavelas.bookworm.resources.BookResource;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookResourceToBook bookConverter;
    private final BookToBookResource bookResourceConverter;

    public BookService(BookRepository bookRepository, BookResourceToBook bookConverter, BookToBookResource bookResourceConverter) {
        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.bookResourceConverter = bookResourceConverter;
    }


    public BookResource createNewBook(BookResource resource) {
        validateBookResource(resource);

        Book book = bookConverter.convert(resource);
        Book createdBook = bookRepository.save(book);

        return bookResourceConverter.convert(createdBook);
    }

    private void validateBookResource(BookResource resource) {
        if (!ISBNValidator.getInstance().isValid(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN");
        }

        if (bookRepository.existsByIsbn(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN. A book with the same ISBN already exists.");
        }
    }
}
