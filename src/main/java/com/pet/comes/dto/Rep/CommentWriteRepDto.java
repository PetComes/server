package com.pet.comes.dto.Rep;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentWriteRepDto {
    private String username;
    private String imageurl;
    private String aftertime;
    private String text;


    public CommentWriteRepDto(String username, String imageurl, String aftertime, String text){
        this.aftertime = aftertime;
        this.username = username;
        this.imageurl = imageurl;
        this.text = text;
    }
}
