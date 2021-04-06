package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.AuthorResourceToAuthor;
import com.mantzavelas.bookworm.converters.AuthorToAuthorResource;
import com.mantzavelas.bookworm.exceptions.ResourceAlreadyExistsException;
import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.resources.AuthorResource;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorResourceToAuthor authorResourceConverter;
    private final AuthorToAuthorResource authorConverter;

    public AuthorService(AuthorRepository authorRepository, AuthorResourceToAuthor authorResourceConverter, AuthorToAuthorResource authorConverter) {
        this.authorRepository = authorRepository;
        this.authorResourceConverter = authorResourceConverter;
        this.authorConverter = authorConverter;
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
}
