package com.pet.comes.controller;

import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {


    private final CommentService commentService;

    /* D8 : 다이어리 댓글 작성 API --Tony*/
    @PostMapping()
    public ResponseEntity writeComment(@RequestBody CommentReqDto commentReqDto) {
        return commentService.writeComment(commentReqDto);
    }

    /* D6 : 다이어리 댓글 상세보기 API --Tony*/
    @GetMapping("/{diaryId}")
    public ResponseEntity readComments(@PathVariable Long diaryId){
        return commentService.readComments(diaryId);
    }
}
