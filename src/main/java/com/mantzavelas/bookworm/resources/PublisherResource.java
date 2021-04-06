package com.mantzavelas.bookworm.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherResource {

    private Long id;

    private String name;

    private String telephone;

    private String address;
}
