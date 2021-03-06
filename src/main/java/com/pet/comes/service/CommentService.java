package com.pet.comes.service;

import com.pet.comes.dto.Rep.CommentListRepDto;
import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.model.Entity.*;
import com.pet.comes.repository.AlarmRepository;
import com.pet.comes.repository.CommentRepository;
import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final Status status;
    private final ResponseMessage message;
    private final AlarmRepository alarmRepository;

    /* D6 : 다이어리 댓글 상세보기 API -- Tony */
    public ResponseEntity readComments(Long diaryId) {

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        if (!diary.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARY), HttpStatus.OK);
        }

        // 댓글내용, 시간 등 불러오기
        List<Object[]> queries = commentRepository.findAllByDiaryIdForD6(diaryId);
        Iterator iterator = queries.listIterator();
        List<CommentListRepDto> dtos = new ArrayList<>();

        while (iterator.hasNext()) {
            Object[] comments = (Object[]) iterator.next();

            String nickname = (String) comments[0];
            String imageUrl = (String) comments[1];
            String text = (String) comments[2];
            LocalDateTime date = (LocalDateTime) comments[3];
            String commentedAt = date.format(DateTimeFormatter.ofPattern("yyyMMdd"));
            Long commentCommentId = (Long) comments[4];
            Long commentId = (Long) comments[5];

            CommentListRepDto commentListRepDto = new CommentListRepDto(nickname, imageUrl, text, commentedAt, commentCommentId,commentId);
            dtos.add(commentListRepDto);
        }


        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "다이러리 댓글 조회 " + message.SUCCESS + "해당 댓글들의 다이어리 id : " + diary.get().getId(), dtos), HttpStatus.OK);

    }

    /* D8 : 다이어리 댓글 작성 API --Tony */
    public ResponseEntity writeComment(CommentReqDto commentReqDto) {
        if (commentReqDto.getText() == null || commentReqDto.getText().length() < 1) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_ENTERED + " 댓글이 비어있습니다."), HttpStatus.OK);
        }
        Optional<User> isExistUser = userRepository.findById(commentReqDto.getUserId());
        if (!isExistUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_ENTERED + "해당 유저가 없습니다."), HttpStatus.OK);

        Optional<Diary> isExist = diaryRepository.findById(commentReqDto.getDiaryId());
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARY), HttpStatus.OK);

        Diary diary = isExist.get();
        int commentsCnt = diary.getHowManyComments();
        diary.setHowManyComments(commentsCnt + 1); // 댓글 카운트 하나 증가

        User user = isExistUser.get();
        Comment comment = new Comment(commentReqDto, user);
        commentRepository.save(comment);

        /* 댓글 작성시 alarm 등록 */
        Alarm alarm = new Alarm(diary.getUser(), 1, 0, comment.getCommentId(), diary); // 해당 다이어리의 주인(알림을 받을 유저),type = 1 : 댓글 / isChecked = 0 : 읽지 않음
        alarmRepository.save(alarm);

        if (comment.getCommentCommentId() != null) // 대댓글 달기라면
            return new ResponseEntity(DataResponse.response(status.SUCCESS,
                    "대댓글 작성 " + message.SUCCESS + "해당 대댓글 다이어리 id : " + comment.getDiaryId(), comment.getDiaryId()), HttpStatus.OK);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "다이러리 댓글 작성 " + message.SUCCESS + "해당 댓글 다이어리 id : " + comment.getDiaryId(), comment.getDiaryId()), HttpStatus.OK);
    }

    /* D9 : 다이어리 댓글 삭제 API --Tony */
    public ResponseEntity deleteComment(Long commentId, Long userId) {

        // 댓글 존재하는지 유효성 검사
        Optional<Comment> byId = commentRepository.findById(commentId);
        if (!byId.isPresent()) // 댓글 존재 x
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "해당 댓글이 존재하지 않습니다."), HttpStatus.NOT_FOUND);
        Comment comment = byId.get(); // 댓글 존재

        // 유저 유효성 검사
        Optional<User> byId1 = userRepository.findById(userId);
        if (!byId1.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.NOT_FOUND);
        User user = byId1.get();

        if (user.getId() != comment.getUser().getId())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "해당유저는 삭제 권한이 없습니다."), HttpStatus.NOT_FOUND);

        comment.setText("(삭제된 댓글)"); // 댓글 삭제

        commentRepository.save(comment);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "다이러리 댓글 삭제 " + message.SUCCESS + "해당 댓글 다이어리 id : " + comment.getDiaryId(), comment.getDiaryId()), HttpStatus.OK);

    }

}
