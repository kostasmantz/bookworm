package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
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
        return createOrUpdateBook(null, resource);
    }

    public BookResource createOrUpdateBook(Long bookId, BookResource resource) {
        validateBookResource(resource, bookId == null);

        if (bookId != null && !bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id " + bookId);
        }

        Book book = bookConverter.convert(resource);
        book.setId(bookId);
        Book createdBook = bookRepository.save(book);

        return bookResourceConverter.convert(createdBook);
    }

    private void validateBookResource(BookResource resource, boolean isNew) {
        if (!ISBNValidator.getInstance().isValid(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN");
        }

        if (isNew && bookRepository.existsByIsbn(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN. A book with the same ISBN already exists.");
        }
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id " + bookId);
        }

        bookRepository.deleteById(bookId);
    }
}
