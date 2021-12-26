package com.pet.comes.dto.Req;

import com.pet.comes.model.EnumType.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // final선언한 멤버변수에대한 생성자를 만들어줌
public class DogReqDto {
    private final String kindOf; // 견종
    private final String name;
    private final int age;
    private final String imageUrl;
    private final String birthday;
    private final int breedId; // 견종
    private final float weight;
    private final Sex sex;
    private final int isNeutered; // 중성화 여부
    private final Long registerationNo ; // 반려견 등록번호



}
