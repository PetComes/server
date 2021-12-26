package com.pet.comes.dto.Rep;


import com.pet.comes.model.Entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryListRepDto {
    //    private final
//    private String username; -> family 와 User가 단방향이고 familyId 를 가진 User가 여럿이기 때문에 유저정보 get api로 찾아오는것이 더 빠를듯
    private String createdAt;
    private String text;
    private int commentCount;
    private int pinCount;
    private Long diaryId;
//    public DiaryListRepDto(String createdAt, String text, int commentCount, int pinCount) {
//        this.username =
//        this.createdAt = createdAt;
//        this.text = text;
//        this.commentCount = commentCount;
//        this.pinCount = pinCount;
//
//    }
}
