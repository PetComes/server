package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.*;
import com.pet.comes.model.EnumType.DogSize;
import com.pet.comes.model.EnumType.DogStatus;
import com.pet.comes.model.EnumType.Sex;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "dog")
public class Dog  {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ManyToOne : default fetchType = EGAER
    @JoinColumn(name="family_id")
    private Family family; // 외래키 : Dog.family 가 연관관계의 주인임. 주인만이 외래 키를 관리(등록,삭제,수정) 가능, 주인이 아닌 엔티티는 읽기만 가능

    @Enumerated(value=EnumType.STRING)
    private DogStatus status;

    private int breedId;

    @Enumerated(value=EnumType.STRING)
    private DogSize size;

    private String name;
    private int age;
    private String birthday;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private float weight;

    @OneToMany(mappedBy = "dog", fetch = FetchType.LAZY)
    private List<DogLog> bodyInfoLogs = new ArrayList<>();

    @Enumerated(value=EnumType.STRING)
    private Sex sex;


    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private int isNeutered;

    private String registerationNo;

    private Long modifiedBy;

    public Dog(Long userId, DogReqDto dogReqDto){
        this.breedId = dogReqDto.getBreedId();
        this.name = dogReqDto.getName();
        this.age = dogReqDto.getAge();
        this.weight = dogReqDto.getWeight();
        this.birthday = dogReqDto.getBirthday();
        this.imageUrl = dogReqDto.getImageUrl();
        this.sex = dogReqDto.getSex();
        this.isNeutered = dogReqDto.getIsNeutered();
//        this.registerationNo = dogReqDto.getRegisterationNo();
        this.modifiedBy = userId;
    }


    public void setFamily(Family family){ // 양방향 매핑 : 양방향 관계는 항상 서로를 참조해야됨.
        // 양뱡향에서 다대일측(Dog) 에서 기존 연관관계를 끊기
//        if(this.family != null) { // 기존의 family가 있으면
//            this.family.getDogs().remove(this); // Family의 dog에서 삭제
//        }

        this.family = family;
        if(!family.getDogs().contains(this)) { // 무한 루프 방지
            family.getDogs().add(this);
        }
    }

    public void addDogLog(DogLog dogLog) { // bodyInfoLogs에서 DogLog 객체를 항상 갖고 있게 하기 위해 add 메서드 추가
        this.bodyInfoLogs.add(dogLog);
        if(dogLog.getDog() != this) {
            dogLog.setDog(this);
        }
    }

}
