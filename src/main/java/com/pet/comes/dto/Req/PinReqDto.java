package com.pet.comes.dto.Req;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PinReqDto {

    private final Long userId;
    private final Long diaryId;
}
