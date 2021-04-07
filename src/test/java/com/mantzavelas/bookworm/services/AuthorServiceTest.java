package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.AuthorResourceToAuthor;
import com.mantzavelas.bookworm.converters.AuthorToAuthorResource;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.ResourceAlreadyExistsException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.resources.AuthorResource;
import com.mantzavelas.bookworm.resources.BookResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    public static final String AUTHOR_NAME = "John";
    public static final String AUTHOR_LAST_NAME = "Doe";
    public static final String AUTHOR_EMAIL = "john.doe@gmail.com";
    public static final LocalDate AUTHOR_DATE_OF_BIRTH = LocalDate.of(1975, 5, 23);
    public static final long AUTHOR_ID = 1L;

    @Mock
    private AuthorRepository repository;
    private AuthorService service;

    private AuthorResource resource;
    private Author author;

    @BeforeEach
    void setUp() {
        service = new AuthorService(repository, new AuthorResourceToAuthor(), new AuthorToAuthorResource(), new BookToBookResource());
        resource = AuthorResource.builder()
            .firstName(AUTHOR_NAME)
            .lastName(AUTHOR_LAST_NAME)
            .email(AUTHOR_EMAIL)
            .dateOfBirth(AUTHOR_DATE_OF_BIRTH)
            .build();

        author = new Author(AUTHOR_ID, AUTHOR_NAME, AUTHOR_LAST_NAME, AUTHOR_EMAIL, AUTHOR_DATE_OF_BIRTH, new ArrayList<>());
        author.getBooks().add(new Book(1L, "Dummy book", "dummy description", BookStatus.LIVE, LocalDate.now(), "dsadsa", author, null));
    }

    @Test
    void createNewAuthorWithExistingEmail_ShouldThrowException() {
        when(repository.existsByEmail(any())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.createNewAuthor(resource));
    }

    @Test
    void createNewAuthorWithValidResource_ShouldCreateAuthor() {
        when(repository.existsByEmail(any())).thenReturn(false);
        when(repository.save(any())).thenReturn(author);

        AuthorResource savedAuthor = service.createNewAuthor(resource);

        assertEquals(AUTHOR_ID, savedAuthor.getId());
        assertEquals(AUTHOR_NAME, savedAuthor.getFirstName());
    }

    @Test
    void testGetBooksForAuthorNotFound_ShouldThrowException() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getBooks(AUTHOR_ID));
    }

    @Test
    void testGetBooks_ShouldReturnBooks() {
        when(repository.findById(any())).thenReturn(Optional.of(author));

        List<BookResource> bookResources = service.getBooks(AUTHOR_ID);

        assertEquals(1, bookResources.size());
        assertEquals(author.getId(), bookResources.get(0).getAuthorId());
    }
}