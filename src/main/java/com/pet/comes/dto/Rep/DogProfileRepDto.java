package com.pet.comes.dto.Rep;

import com.pet.comes.model.EnumType.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DogProfileRepDto {
    private String imageurl;
    private String birthday;
    private float weight;
    private Sex sex ; // enum 타입 : F,M
    private String name;
    private String breed;  // db에는 breedId로 저장했음. 따로 정해줘야됨.
    private int age;
}
