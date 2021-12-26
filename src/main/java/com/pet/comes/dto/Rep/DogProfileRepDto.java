package com.pet.comes.dto.Rep;

import com.pet.comes.model.Entity.Dog;
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


    public DogProfileRepDto(Dog dog){
        this.imageurl = dog.getImageUrl();
        this.birthday = dog.getBirthday();
        this.weight = dog.getWeight();
        this.sex = dog.getSex();
        this.name = dog.getName();
        this.breed = breedIdtoString(dog.getBreedId());
        this.age = dog.getAge();
    }

    public String breedIdtoString(int breedId) {
        String breed = "";
        switch (breedId) {
            case 1:
                breed = "진돗개";
                break;
            case 21:
                breed = "토이푸들";
                break;

            case 23:
                breed = "시바";
                break;
            case 43:
                breed = "요크셔테리어";
                break;
            default:
                breed = "해당강아지종없음 : 강아지 품종을 설정해 주세요.";
                break;
        }
        return breed;
    }
}
