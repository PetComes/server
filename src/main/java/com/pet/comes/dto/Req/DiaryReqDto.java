package com.pet.comes.dto.Req;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class DiaryReqDto {

    private final Long userId;
    private final String Text;
    private final int howManyDogs;
    private final Long addressId;
    private final int isPublic; // 1: 공개 , 0 : 비공개
    private final int isDeleted; // 1: 삭제 , 0 : 정상
    private LocalDateTime registedAt;
    private LocalDateTime modifiedAt;


}
