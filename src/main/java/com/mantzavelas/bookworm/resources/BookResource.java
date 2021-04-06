package com.mantzavelas.bookworm.resources;

import com.mantzavelas.bookworm.models.BookStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResource {

    private Long id;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    private String description;

    @NotNull(message = "Status cannot be null")
    private BookStatus status;

    private LocalDate createdAt;

    @NotEmpty(message = "ISBN cannot be empty")
    private String isbn;

    private Long authorId;

    private Long publisherId;
}
