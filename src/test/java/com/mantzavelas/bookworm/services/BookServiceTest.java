package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.repositories.BookRepository;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.BookResource;
import com.mantzavelas.bookworm.resources.PublisherResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "Dummy book";
    private static final String VALID_ISBN = "9780684800011";

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private PublisherRepository publisherRepository;

    private BookService service;

    private Book book;

    @BeforeEach
    void setUp() {
        service = new BookService(bookRepository, new BookResourceToBook(authorRepository, publisherRepository), new BookToBookResource());

        book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setStatus(BookStatus.LIVE);
        book.setIsbn(VALID_ISBN);
    }

    @Test
    void testCreateNewBookWithInvalidIsbn_ShouldThrowException() {
        BookResource resource = BookResource.builder()
            .title("Dummy book")
            .status(BookStatus.LIVE)
            .isbn("123456")
            .build();

        assertThrows(InvalidIsbnException.class, () ->service.createNewBook(resource));
    }

    @Test
    void testCreateNewBookWithValidAndExistingIsbn_ShouldThrowException() {
        when(bookRepository.existsByIsbn(any())).thenReturn(true);

        BookResource resource = BookResource.builder()
            .title("Dummy book")
            .status(BookStatus.LIVE)
            .isbn(VALID_ISBN)
            .build();

        assertThrows(InvalidIsbnException.class, () ->service.createNewBook(resource));
    }

    @Test
    void testCreateNewBookWithValidIsbn_ShouldCreateBook() {
        when(bookRepository.existsByIsbn(any())).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(book);

        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .status(BookStatus.LIVE)
                .isbn(VALID_ISBN)
                .build();

        BookResource savedBook = service.createNewBook(resource);

        assertNotNull(savedBook);
        assertEquals(BOOK_ID, savedBook.getId());
        assertNull(savedBook.getAuthorId());
        assertNull(savedBook.getPublisherId());
    }
}