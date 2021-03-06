package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.BookResourceToBook;
import com.mantzavelas.bookworm.converters.BookToBookDetailsResource;
import com.mantzavelas.bookworm.converters.BookToVisibleBookResource;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.InvalidIsbnException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.repositories.BookRepository;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.BookDetailsResource;
import com.mantzavelas.bookworm.resources.BookResource;
import com.mantzavelas.bookworm.resources.VisibleBookResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    List<Book> books;

    @BeforeEach
    void setUp() {
        service = new BookService(bookRepository,
                authorRepository, publisherRepository, new BookResourceToBook(authorRepository, publisherRepository),
                new BookToBookResource(),
                new BookToVisibleBookResource(),
                new BookToBookDetailsResource());

        book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setStatus(BookStatus.CREATING);
        book.setIsbn(VALID_ISBN);

        updated = new Book();
        updated.setId(BOOK_ID);
        updated.setTitle("New title");
        updated.setStatus(BookStatus.LIVE);
        updated.setIsbn(VALID_ISBN);

        books = List.of(
                book,
                new Book(2L, "Another Title"
                        , "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum."
                        , BookStatus.LIVE, LocalDate.now(), "465456465", null, new Publisher())
        );
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
        verifyNoInteractions(authorRepository);
        verifyNoInteractions(publisherRepository);
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
        verifyNoInteractions(authorRepository);
        verifyNoInteractions(publisherRepository);
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

    @Test
    void testFindAllVisible_ShouldReturnList() {
        when(bookRepository.findAllByStatus(any())).thenReturn(books);

        List<VisibleBookResource> bookResources = service.findAllVisible();

        verify(bookRepository).findAllByStatus(any());
        assertEquals(1, bookResources.size());
        assertTrue(bookResources.get(0).getDescription().length() <= 103);
    }

    @Test
    void testGetDetailsForBookWhenNotFound_ShouldThrowException() {
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getDetailsForBook(1L));
    }

    @Test
    void testGetDetailsForBook_ShouldReturnResource() {
        when(bookRepository.findById(any())).thenReturn(Optional.of(book));

        BookDetailsResource resource = service.getDetailsForBook(1L);

        assertEquals(book.getTitle(), resource.getTitle());
        assertNull(resource.getAuthorEmail());
    }
}