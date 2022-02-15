package com.pet.comes.dto.Rep;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DiaryUserDto {
    private final Long id;
    private final String text;
    private final String diaryImageUrl;
    private final int isPublic;
    private final int  howManyComments;

    private final String userImageUrl;
    private final String nickName;
    private final Long userId;

}
