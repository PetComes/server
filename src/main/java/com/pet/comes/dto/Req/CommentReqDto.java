package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentReqDto {

    private final Long userId;
    private final Long diaryId;
    private final String text;
    private final Long commentCommentId;


}
