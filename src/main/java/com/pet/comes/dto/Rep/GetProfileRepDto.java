package com.pet.comes.dto.Rep;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class GetProfileRepDto extends AddDogsRepDto {
    private String nickName;
    private String imagUrl; // 뱃지 관련 임시(?)
    private String introduction;
    private String badge;

    public GetProfileRepDto(String nickName, String imageUrl, String introduction,String badge) {
        this.nickName = nickName;
        this.imagUrl = imageUrl;
        this.introduction =introduction;
        this.badge = badge;
    }

}
