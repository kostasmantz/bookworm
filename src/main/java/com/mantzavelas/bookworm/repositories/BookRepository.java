package com.mantzavelas.bookworm.repositories;

import com.mantzavelas.bookworm.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
