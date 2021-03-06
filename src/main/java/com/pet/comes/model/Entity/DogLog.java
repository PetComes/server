package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.DogBodyInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "dog_log")
public class DogLog implements Serializable {

    // @EmbeddedId
    // private DogLogId dog = new DogLogId();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    private float weight;

    @CreatedDate
    private LocalDateTime registeredAt = LocalDateTime.now();

    public void setDog(Dog dog) {
        if (!dog.getBodyInfoLogs().contains(this)) {
            dog.addDogLog(this);
        }
    }

    public DogLog(DogBodyInformationDto dogBodyInformationDto, Dog dog) {
        this.dog = dog;

        if (!dog.getBodyInfoLogs().contains(this)) {
            dog.getBodyInfoLogs().add(this);
        }
        this.weight = dogBodyInformationDto.getWeight();

    }

    public DogLog(Dog dog, float weight) {
        this.dog = dog;

        if(!dog.getBodyInfoLogs().contains(this)) {
            dog.getBodyInfoLogs().add(this);
        }
        this.weight = weight;
    }
}
