package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.resources.BookDetailsResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookToBookDetailsResource implements Converter<Book, BookDetailsResource> {

    @Override
    public BookDetailsResource convert(Book book) {
        BookDetailsResource resource = new BookDetailsResource();
        resource.setTitle(book.getTitle());
        resource.setDescription(book.getDescription());
        resource.setIsbn(book.getIsbn());
        resource.setAuthorName(Optional.ofNullable(book.getAuthor())
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .orElse(null)
        );
        resource.setCreatedAt(book.getCreatedAt());
        resource.setAuthorEmail(Optional.ofNullable(book.getAuthor()).map(Author::getEmail).orElse(null));
        resource.setAuthorDateOfBirth(Optional.ofNullable(book.getAuthor()).map(Author::getDateOfBirth).orElse(null));
        resource.setPublisherName(Optional.ofNullable(book.getPublisher()).map(Publisher::getName).orElse(null));
        resource.setPublisherAddress(Optional.ofNullable(book.getPublisher()).map(Publisher::getAddress).orElse(null));

        return resource;
    }
}
