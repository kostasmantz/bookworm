package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.AuthorResourceToAuthor;
import com.mantzavelas.bookworm.converters.AuthorToAuthorResource;
import com.mantzavelas.bookworm.converters.BookToBookResource;
import com.mantzavelas.bookworm.exceptions.ResourceAlreadyExistsException;
import com.mantzavelas.bookworm.exceptions.ResourceNotFoundException;
import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.resources.AuthorResource;
import com.mantzavelas.bookworm.resources.BookResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorResourceToAuthor authorResourceConverter;
    private final AuthorToAuthorResource authorConverter;
    private final BookToBookResource bookConverter;

    public AuthorService(AuthorRepository authorRepository, AuthorResourceToAuthor authorResourceConverter, AuthorToAuthorResource authorConverter, BookToBookResource bookConverter) {
        this.authorRepository = authorRepository;
        this.authorResourceConverter = authorResourceConverter;
        this.authorConverter = authorConverter;
        this.bookConverter = bookConverter;
    }

    public AuthorResource createNewAuthor(AuthorResource resource) {
        validateResource(resource);

        Author author = authorResourceConverter.convert(resource);

        Author savedAuthor = authorRepository.save(author);

        return authorConverter.convert(savedAuthor);
    }

    private void validateResource(AuthorResource resource) {
        if (authorRepository.existsByEmail(resource.getEmail())) {
            throw new ResourceAlreadyExistsException("Author already exists with email " + resource.getEmail());
        }
    }

    public List<BookResource> getBooks(Long authorId) {
        return authorRepository.findById(authorId)
            .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId))
            .getBooks()
            .stream()
            .map(bookConverter::convert)
            .collect(Collectors.toList());
    }
}
