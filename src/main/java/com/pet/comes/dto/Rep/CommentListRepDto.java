package com.pet.comes.dto.Rep;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListRepDto {

    private String nickname;
    private String imageurl;
    private String text;
    private String aftertime;
    private Long commentCommentId;
    private Long commentId;



}
