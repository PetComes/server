package com.pet.comes.dto.Rep;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class DogsProfileRepDto {

    private String dogName;
    private String dogImageUrl;

    // 반려견 1마리
    public DogsProfileRepDto(String dogName, String dogImageUrl){
        this.dogName = dogName;
        this.dogImageUrl = dogImageUrl;
    }


}
