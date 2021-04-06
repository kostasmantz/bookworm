package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.repositories.BookRepository;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.BookResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final Long BOOK_ID = 1L;
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
    private Book updated;

    @BeforeEach
    void setUp() {
        service = new BookService(bookRepository, new BookResourceToBook(authorRepository, publisherRepository), new BookToBookResource());

        book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setStatus(BookStatus.LIVE);
        book.setIsbn(VALID_ISBN);

        updated = new Book();
        updated.setId(BOOK_ID);
        updated.setTitle("New title");
        updated.setStatus(BookStatus.LIVE);
        updated.setIsbn(VALID_ISBN);
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

    @Test
    void testCreateOrUpdateBookWithInvalidIsbn_ShouldThrowException() {
        BookResource resource = BookResource.builder()
                .title("Dummy book")
                .status(BookStatus.LIVE)
                .isbn("123456")
                .build();

        assertThrows(InvalidIsbnException.class, () ->service.createOrUpdateBook(1L, resource));
    }

    @Test
    void testCreateOrUpdateBookWithValidIsbnButNotFoundId_ShouldThrowException() {
        when(bookRepository.existsById(any())).thenReturn(false);

        BookResource resource = BookResource.builder()
                .title(BOOK_TITLE)
                .status(BookStatus.LIVE)
                .isbn(VALID_ISBN)
                .build();

        assertThrows(ResourceNotFoundException.class, () -> service.createOrUpdateBook(1L, resource));
    }

    @Test
    void testCreateOrUpdateBookWithValidIsbn_ShouldUpdate() {
        when(bookRepository.existsById(any())).thenReturn(true);
        when(bookRepository.save(any())).thenReturn(updated);

        BookResource resource = BookResource.builder()
                .title("New title")
                .status(BookStatus.LIVE)
                .isbn(VALID_ISBN)
                .build();

        BookResource updatedBook = service.createOrUpdateBook(1L, resource);

        assertEquals(updatedBook.getId(), updated.getId());
        assertEquals(updatedBook.getTitle(), updated.getTitle());
    }

    @Test
    void testDeleteBookForNonExistentId_ShouldThrowException() {
        when(bookRepository.existsById(any())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteBook(BOOK_ID));
    }

    @Test
    void testDeleteBookForExistentId_ShouldDelete() {
        when(bookRepository.existsById(any())).thenReturn(true);

        service.deleteBook(BOOK_ID);

        verify(bookRepository).deleteById(BOOK_ID);
    }
}