package com.pet.comes.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;

@Getter
@RequiredArgsConstructor
public class FamilyDto {

    private final Long userId;

    private final Long familyId;

    private final int isUser; // 가족 id 가 유저이면 1, 강아지면 0

}
