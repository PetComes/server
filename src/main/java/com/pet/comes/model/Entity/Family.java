package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "family_id")
    private Long id;

//    private Long dogId;

    @OneToMany(mappedBy = "family")
    private List<Dog> dogs = new ArrayList<Dog>();

    public void setDogs(Dog dog) {
        dogs.add(dog);
    }

    private LocalDateTime createdAt;


}
