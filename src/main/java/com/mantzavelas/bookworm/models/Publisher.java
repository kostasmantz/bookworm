package com.mantzavelas.bookworm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String telephone;

    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publisher")
    private List<Book> books = new ArrayList<>();
}
