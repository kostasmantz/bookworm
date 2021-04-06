package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.repositories.AuthorRepository;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.BookResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookResourceToBook implements Converter<BookResource, Book> {

    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;

    public BookResourceToBook(AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Book convert(BookResource bookResource) {
        Book book = new Book();
        book.setTitle(bookResource.getTitle());
        book.setDescription(bookResource.getDescription());
        book.setAuthor(Optional.ofNullable(bookResource.getAuthorId()).flatMap(authorRepository::findById).orElse(null));
        book.setPublisher(Optional.ofNullable(bookResource.getPublisherId()).flatMap(publisherRepository::findById).orElse(null));
        book.setCreatedAt(bookResource.getCreatedAt());
        book.setIsbn(bookResource.getIsbn());
        book.setStatus(bookResource.getStatus());

        return book;
    }
}
