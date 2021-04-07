package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookDetailsResource;
import com.mantzavelas.bookworm.converters.BookToVisibleBookResource;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.repositories.BookRepository;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.BookDetailsResource;
import com.mantzavelas.bookworm.resources.VisibleBookResource;
import com.mantzavelas.bookworm.resources.BookResource;
import org.apache.commons.validator.routines.ISBNValidator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    private final BookResourceToBook bookConverter;
    private final BookToBookResource bookResourceConverter;
    private final BookToVisibleBookResource visibleBookResourceConverter;
    private final BookToBookDetailsResource bookDetailsConverter;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository,
                       BookResourceToBook bookConverter, BookToBookResource bookResourceConverter,
                       BookToVisibleBookResource visibleBookResourceConverter, BookToBookDetailsResource bookDetailsConverter) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.bookConverter = bookConverter;
        this.bookResourceConverter = bookResourceConverter;
        this.visibleBookResourceConverter = visibleBookResourceConverter;
        this.bookDetailsConverter = bookDetailsConverter;
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

        Book savedBook = bookRepository.save(book);
        updateBookAuthor(savedBook);
        updateBookPublisher(savedBook);

        return bookResourceConverter.convert(savedBook);
    }

    private void validateBookResource(BookResource resource, boolean isNew) {
        if (!ISBNValidator.getInstance().isValid(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN");
        }

        if (isNew && bookRepository.existsByIsbn(resource.getIsbn())) {
            throw new InvalidIsbnException("Invalid ISBN. A book with the same ISBN already exists.");
        }
    }


    private void updateBookPublisher(Book savedBook) {
        if (savedBook.getPublisher() != null) {
            Publisher publisher = savedBook.getPublisher();
            publisher.getBooks().add(savedBook);
        }
    }

    private void updateBookAuthor(Book savedBook) {
        if (savedBook.getAuthor() != null) {
            Author author = savedBook.getAuthor();
            author.getBooks().add(savedBook);
            authorRepository.save(author);
        }
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id " + bookId);
        }

        bookRepository.deleteById(bookId);
    }

    public List<VisibleBookResource> findAllVisible() {
        List<Book> books = bookRepository.findAllByStatus(BookStatus.LIVE);

        return books.stream()
            .filter(book -> book.getPublisher() != null)
            .sorted(Comparator.comparing(((Function<Book, Author>)Book::getAuthor).andThen(Author::getLastName))
                    .thenComparing(Collections.reverseOrder(Comparator.comparing(Book::getId))))
            .map(visibleBookResourceConverter::convert)
            .collect(Collectors.toList());
    }

    public BookDetailsResource getDetailsForBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));

        return bookDetailsConverter.convert(book);
    }
}
