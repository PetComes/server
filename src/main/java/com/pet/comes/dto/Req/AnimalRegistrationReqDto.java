package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class AnimalRegistrationReqDto {

    private long userId;
    private LocalDate birthday;

    private long dogId;
    private String dogRegNo;
}
