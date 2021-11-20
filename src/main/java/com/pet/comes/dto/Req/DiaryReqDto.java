package com.pet.comes.dto.Req;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DiaryReqDto {

    private final Long userId;
    private final Long dogId;
    private final int howManyDogs;
    private final int isPublic; // 1: 공개 , 0 : 비공개
    private final String Text;
    private final Long addressId;
    private final int isDeleted; // default : 0
    private final String imageUrl;

}
