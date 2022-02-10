package com.pet.comes.dto.Rep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileRepDto extends UserProfileAddRepDto {
    private String nickName;
    private String imagUrl; // 뱃지 관련 임시(?)

    public GetProfileRepDto(String nickName, String imageUrl, String introduction, String badge) {
        super(introduction,badge);
        this.nickName = nickName;
        this.imagUrl = imageUrl;
    }

}
