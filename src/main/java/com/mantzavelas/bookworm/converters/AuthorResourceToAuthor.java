package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.resources.AuthorResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorResourceToAuthor implements Converter<AuthorResource, Author> {

    @Override
    public Author convert(AuthorResource resource) {
        Author author = new Author();
        author.setFirstName(resource.getFirstName());
        author.setLastName(resource.getLastName());
        author.setEmail(resource.getEmail());
        author.setDateOfBirth(resource.getDateOfBirth());

        return author;
    }
}
