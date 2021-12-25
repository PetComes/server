package com.pet.comes.dto.Rep;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRepDto {
    private String imageurl;
    private String nickname;
//    private String badge;  // 뱃지 추가해야됨.
    private List<String> dognames;

}
