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

    @OneToMany(mappedBy = "family") // 연관관계 주인 : 주인만이 왜래 키를 관리(등록,삭제,수정) 가능, 주인이 아닌 엔티티는 읽기만 가능
    private List<Dog> dogs = new ArrayList<Dog>(); // 양방향


    public void setDogs(Dog dog) { // 양방향 매핑
        // 무한루프 참고 : https://velog.io/@gillog/JPA-%EC%96%91%EB%B0%A9%ED%96%A5-%EB%A7%A4%ED%95%91%EA%B3%BC-%EC%97%B0%EA%B4%80-%EA%B4%80%EA%B3%84-%EC%A3%BC%EC%9D%B8
        this.dogs.add(dog);

        if(dog.getFamily() != this) {
            dog.setFamily(this);
        }
    }


    private LocalDateTime createdAt;


}
