package com.pet.comes.dto;

import com.pet.comes.model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyFamilyDto {
    private  Long id;
    private  String name;
    private  String imageUrl;


    public MyFamilyDto(User user){
            this.id = user.getId();
            this.name = user.getName();
            this.imageUrl = user.getImageUrl();;
    }


}
