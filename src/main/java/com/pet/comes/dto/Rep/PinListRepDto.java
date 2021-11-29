package com.pet.comes.dto.Rep;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PinListRepDto {

    private String nickname; // User
    private String profileImageUrl; // User
//    private String createdAt; // Diary
    private String text; // Diary
    private String contentImageUrl; // Diary
//    private int commentCount; // Comment
//    private int pinCount; // Pin

}