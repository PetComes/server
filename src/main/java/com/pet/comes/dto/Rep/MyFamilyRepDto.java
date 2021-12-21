package com.pet.comes.dto.Rep;

import com.pet.comes.model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyFamilyRepDto {
    private  Long id;
    private  String name;
    private  String imageUrl;


    public MyFamilyRepDto(User user){
            this.id = user.getId();
            this.name = user.getName();
            this.imageUrl = user.getImageUrl();;
    }


}
