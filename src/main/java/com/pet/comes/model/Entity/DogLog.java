package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.DogBodyInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "dog_log")
public class DogLog implements Serializable {

    @Id
    @Column(name="dog_id")
    private Long dogId;
    private float weight;
    private LocalDateTime registeredAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="id")
    private Dog dog; // @JoinColumn(name="dog_id")

    public void setDog(Dog dog) {
        this.dog = dog;

        if(!dog.getBodyInfoLogs().contains(this)) {
            dog.addDogLog(this);
        }
    }

    public DogLog(DogBodyInformationDto dogBodyInformationDto) {
        //this.dogId = dogBodyInformationDto.getDogId();
        this.weight = dogBodyInformationDto.getWeight();
    }
}
