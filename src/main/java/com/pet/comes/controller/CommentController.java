package com.pet.comes.controller;

import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {


    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity writeComment(@RequestBody CommentReqDto commentReqDto) {
        return commentService.writeComment(commentReqDto);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity readComment(@PathVariable Long diaryId){
        return commentService.readComment(diaryId);
    }
}
