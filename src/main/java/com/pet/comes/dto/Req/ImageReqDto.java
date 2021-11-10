package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Getter
@RequiredArgsConstructor
public class ImageReqDto {
    private final Long diaryId;
    private final String url;

}
