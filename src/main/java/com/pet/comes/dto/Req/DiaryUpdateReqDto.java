package com.pet.comes.dto.Req;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryUpdateReqDto {

    private String imageUrl;
    private int isPublic;
    private String text;

}
