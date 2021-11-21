package com.pet.comes.service;

import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.model.Entity.Comment;
import com.pet.comes.repository.CommentRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final Status status;
    private final ResponseMessage message;


    @Autowired
    public CommentService(UserRepository userRepository, CommentRepository commentRepository, Status status, ResponseMessage message) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.message = message;
        this.status = status;
    }

    /* 다이어리 댓글 작성 API --Tony */
    public ResponseEntity writeComment(CommentReqDto commentReqDto) {
        if (commentReqDto.getText() == null || commentReqDto.getText().length() < 1) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_ENTERED+" 댓글이 비어있습니다."), HttpStatus.OK);
        }

        Comment comment = new Comment(commentReqDto);
        commentRepository.save(comment);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "다이러리 댓글 작성 " + message.SUCCESS + "해당 댓글 다이어리 id : ", comment.getDiaryId()), HttpStatus.OK);
    }
}
