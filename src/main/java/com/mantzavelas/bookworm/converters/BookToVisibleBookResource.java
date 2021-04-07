package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.resources.VisibleBookResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookToVisibleBookResource implements Converter<Book, VisibleBookResource> {

    @Override
    public VisibleBookResource convert(Book book) {
        VisibleBookResource resource = new VisibleBookResource();
        resource.setTitle(book.getTitle());
        resource.setDescription(truncate(book.getDescription(), 100));
        resource.setIsbn(book.getIsbn());
        resource.setAuthorName(Optional.ofNullable(book.getAuthor())
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .orElse(null)
        );

        return resource;
    }

    private String truncate(String value, int chars) {
        return value != null && value.length() > chars
            ? value.substring(0, chars) + "..." : value;
    }
}
