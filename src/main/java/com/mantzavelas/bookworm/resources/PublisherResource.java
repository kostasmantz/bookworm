package com.mantzavelas.bookworm.resources;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResource {

    private Long id;

    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    private String telephone;

    private String address;
}
