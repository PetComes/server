package com.pet.comes.model.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long id;

    private Long dogId;

    @OneToMany(mappedBy = "family")
    private List<Dog> dogs = new ArrayList<Dog>(); // 양방향

    private LocalDateTime createdAt;
}
