package com.mantzavelas.bookworm.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailsResource extends VisibleBookResource {

    private LocalDate createdAt;

    private String authorEmail;

    private LocalDate authorDateOfBirth;

    private String publisherName;

    private String publisherAddress;
}
