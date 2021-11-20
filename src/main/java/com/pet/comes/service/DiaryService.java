package com.pet.comes.service;


import com.pet.comes.dto.Rep.DiaryListRepDto;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public DiaryService( DiaryRepository diaryRepository, Status status, ResponseMessage message) {
        this.diaryRepository = diaryRepository;
        this.status = status;
        this.message = message;
    }

    /* 다이어리 조회 API -- Tony */
    public ResponseEntity dogDiaryList(Long dogId) {

        List<Diary> diaryList = diaryRepository.findAllByDogId(dogId);
        if(diaryList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARIES ), HttpStatus.OK);


        List<DiaryListRepDto> diaryListRepDtoList = new ArrayList<>();
        String createdAt, text;
        int commentCount, pinCount;

        for (Diary diary : diaryList) {
            createdAt = diary.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            text = diary.getText();
            commentCount = 12; // 임의의 값 : 아직 comment 관련 api 만들지않아서
            pinCount = 2; // 임의의 값 : 아직 pin 관련 api 만들지 않아서
            DiaryListRepDto diaryListRepDto = new DiaryListRepDto(createdAt,text,commentCount,pinCount);
            diaryListRepDtoList.add(diaryListRepDto);
        }
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리들 불어오기 " + message.SUCCESS, diaryListRepDtoList), HttpStatus.OK);

    }

    /* 다이어리 작성 API -- Tony */
    public ResponseEntity writeDiary(DiaryReqDto diaryReqDto) {
        if (diaryReqDto.getText() == null)
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + ": 내용이 없습니다."), HttpStatus.OK);

        Diary diary = new Diary(diaryReqDto);
        diaryRepository.save(diary);
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS, diary.getId()), HttpStatus.OK);
    }

}
