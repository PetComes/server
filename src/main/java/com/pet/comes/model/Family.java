package com.pet.comes.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dogId;

    @OneToMany(mappedBy = "family")
    private List<Dog> dogs = new ArrayList<Dog>();

    private LocalDateTime createdAt;
}
