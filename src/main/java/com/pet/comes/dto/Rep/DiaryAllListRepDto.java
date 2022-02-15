package com.pet.comes.dto.Rep;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryAllListRepDto {

    private Long id;
    private String text;
    private String imageUrl;
    private int isPublic;
    private int howManyComments;
    private int howManyPins;
    private LocalDateTime registeredAt;
    private String userImageUrl;
    private String nickName;
    private Long userId;
    private String locationName;


}
