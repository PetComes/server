package com.pet.comes.model.Entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable  // 복합키 매핑시
@Data // equals, hashCode 오버라이드
public class DogLogId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id")
    private Dog dog;

}
