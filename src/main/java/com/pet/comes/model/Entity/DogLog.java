package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.DogBodyInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "dog_log")
public class DogLog {

    @ManyToOne
    @JoinColumn(name="id")
    @Setter(AccessLevel.PROTECTED)
    private Dog dog;

    @Id
    private long dogId = dog.getId();
    // Dog : DogLog = 1 : 다 <=> dogId는 Dog의 id를 참조하는 외래 <=> DogLog가 연관관계의 주인

    private float height;
    private float weight;

    private LocalDateTime registeredAt = LocalDateTime.now();

    public void setDog(Dog dog) {
        this.dog = dog;

        if(!dog.getBodyInfoLogs().contains(this)) {
            dog.getBodyInfoLogs().add(this);
        }
    }

    public DogLog(DogBodyInformationDto dogBodyInformation) {
        this.dogId = dogBodyInformation.getDogId();
        this.height = dogBodyInformation.getHeight();
        this.weight = dogBodyInformation.getWeight();
    }
}
