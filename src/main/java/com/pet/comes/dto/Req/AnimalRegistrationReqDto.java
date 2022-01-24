package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AnimalRegistrationReqDto {

    private long userId;
    private String birthday; // 6자리 : 910522
    private String userName; // 정부에 등록된 소유자가 아닌 가족이 동물등록번호를 등록하는 경우를 대비하여 만들어 둠.

    private long dogId;
    private String dogRegNo;

    public AnimalRegistrationReqDto(String userId, String birthday, String dogId, String dogRegNo) {
        this.userId = Long.parseLong(userId);
        this.birthday = birthday;
        this.dogId = Long.parseLong(dogId);
        this.dogRegNo = dogRegNo;
    }
}
