package com.pet.comes.model.Entity;

import com.pet.comes.model.*;
import com.pet.comes.model.EnumType.DogSize;
import com.pet.comes.model.EnumType.DogStatus;
import com.pet.comes.model.EnumType.Sex;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Dog extends Timestamped {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;

    @Enumerated(value=EnumType.STRING)
    private DogStatus status;

    private Long breedId;

    @Enumerated(value=EnumType.STRING)
    private DogSize size;

    private String name;
    private int age;
    private LocalDateTime birthday;
    private String imageUrl;
    private float height;
    private float weight;

    @Enumerated(value=EnumType.STRING)
    private Sex sex;

    private int isNeutered;
    private Long registrationNo;
    private Long modifiedBy;
}
