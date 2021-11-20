package com.pet.comes.service;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.model.Entity.Diary;
import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository, Status status, ResponseMessage message){
        this.diaryRepository =diaryRepository;
        this.status = status;
        this.message = message;
    }

    public ResponseEntity writeDiary(DiaryReqDto diaryReqDto){
        if(diaryReqDto.getText() == null )
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED+": 내용이 없습니다."), HttpStatus.OK);

        Diary diary = new Diary(diaryReqDto);
        diaryRepository.save(diary);
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 "+message.SUCCESS, diary.getId()),HttpStatus.OK);
    }
}
