package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Author;
import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.resources.BookResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookToBookResource implements Converter<Book, BookResource> {

    @Override
    public BookResource convert(Book book) {
        BookResource resource = new BookResource();
        resource.setId(book.getId());
        resource.setTitle(book.getTitle());
        resource.setDescription(book.getDescription());
        resource.setStatus(book.getStatus());
        resource.setCreatedAt(book.getCreatedAt());
        resource.setIsbn(book.getIsbn());
        resource.setAuthorId(Optional.ofNullable(book.getAuthor()).map(Author::getId).orElse(null));
        resource.setPublisherId(Optional.ofNullable(book.getPublisher()).map(Publisher::getId).orElse(null));

        return resource;
    }
}
