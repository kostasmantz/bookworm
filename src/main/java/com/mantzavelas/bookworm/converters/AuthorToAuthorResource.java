package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.resources.AuthorResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorToAuthorResource implements Converter<Author, AuthorResource> {

    @Override
    public AuthorResource convert(Author author) {
        return AuthorResource.builder()
            .id(author.getId())
            .firstName(author.getFirstName())
            .lastName(author.getLastName())
            .email(author.getEmail())
            .dateOfBirth(author.getDateOfBirth())
            .build();
    }
}
