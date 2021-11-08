package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.PetReqDto;
import com.pet.comes.model.EnumType.DogStatus;
import com.pet.comes.model.EnumType.SexType;
import com.pet.comes.model.EnumType.WeightType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;



@Getter
@Entity
@NoArgsConstructor
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DogStatus status = DogStatus.WITH; // 같이살고 있음, 죽음, 실종

    @Enumerated(value = EnumType.STRING)
    private WeightType type = WeightType.M; // 유형(대,중,소)

    private String kindOf; // 견종

    private String name;

    private int age;

    private String imageUrl;

    private String birthday;

    private String breedId; // 견종

    private float weight;

    private float height;

    @Enumerated(value = EnumType.STRING)
    private SexType sexType = SexType.M; // default M : 수컷이 더 많아서

    @Column(nullable = false, columnDefinition = "TINYINT", length=1) // DBMS의 테이블과 매핑시 오류방지
    private int isNeutered; // 중성화 여부

    private Long registerationNo ; // 반려견 등록번호




    public Dog(@RequestBody PetReqDto petReqDto) {
        this.kindOf = petReqDto.getKindOf();
        this.name = petReqDto.getName();
        this.age = petReqDto.getAge();
        this.imageUrl = petReqDto.getImageUrl();
        this.birthday = petReqDto.getBirthday();
        this.breedId = petReqDto.getBreedId();
        this.weight = petReqDto.getWeight();
        this.height = petReqDto.getHeight();
        this.isNeutered = petReqDto.getIsNeutered();
        this.registerationNo = petReqDto.getRegisterationNo();
    }

    public void update(PetReqDto petReqDto) {
    }


}
