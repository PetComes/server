package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AnimalRegistrationReqDto {

    private long userId;
    private String birthday; //6자리 : 910522

    private long dogId;
    private String dogRegNo;
}
