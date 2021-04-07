package com.mantzavelas.bookworm.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisibleBookResource {

    private String title;

    private String description;

    private String isbn;

    private String authorName;

}
