package com.mantzavelas.bookworm.repositories;

import com.mantzavelas.bookworm.models.Book;
import com.mantzavelas.bookworm.models.BookStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    List<Book> findAllByStatus(BookStatus status);
}
