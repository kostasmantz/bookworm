package com.mantzavelas.bookworm.repositories;

import com.mantzavelas.bookworm.models.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
