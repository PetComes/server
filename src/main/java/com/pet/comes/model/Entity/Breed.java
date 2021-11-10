package com.pet.comes.model.Entity;

import com.pet.comes.model.EnumType.WeightType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name;

    @Enumerated(value = EnumType.STRING)
    private WeightType weightType = WeightType.M;

}
