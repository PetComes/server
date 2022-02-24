package com.pet.comes.dto.Rep;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmListRepDto {

    private Long diaryId;
    private String imageurl;
    private String nickname;
    private String message;
    private LocalDateTime createdAt;
    // private Long diaryId; //다이어리 id 표시해주기
    /*예시
    *
    nickname + "님이 회원님의 게시글에 댓글을 달았습니다.
    nickname + "님이 회원님의 게시글을 핀했습니다.
    *
    * */
}
