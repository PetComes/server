package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.*;
import com.pet.comes.model.EnumType.DogSize;
import com.pet.comes.model.EnumType.DogStatus;
import com.pet.comes.model.EnumType.Sex;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Dog extends Timestamped {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;

    @Enumerated(value=EnumType.STRING)
    private DogStatus status;

    private int breedId;

    @Enumerated(value=EnumType.STRING)
    private DogSize size;

    private String name;
    private int age;
    private String birthday;
    private String imageUrl;
    private float weight;

    @Enumerated(value=EnumType.STRING)
    private Sex sex;

    private int isNeutered;
    private Long registrationNo;
    private Long modifiedBy;

//    @Override
//    public String toString(){
//        return "Dog{"+
//                "createdAt: "+getCreatedAt() +
//                ",modifiedAt: " +getModifiedBy() +
//                ",id: " + id +
//                "}";
//    }

    public Dog(DogReqDto dogReqDto){
        this.breedId = dogReqDto.getBreedId();
        this.name = dogReqDto.getName();
        this.age = dogReqDto.getAge();
        this.weight = dogReqDto.getWeight();
        this.birthday = dogReqDto.getBirthday();
    }


    public void setFamily(Family family){ // 양방향 매핑
        // 양뱡향에서 다대일측(Dog) 에서 기존 연관관계를 끊기
        if(this.family != null) { // 기존의 family가 있으면
            this.family.getDogs().remove(this); // Family의 dog에서 삭제
        }
        this.family = family;
        if(!family.getDogs().contains(this)) { // 무한 루프 방지
            family.setDogs(this);
        }
    }




}
