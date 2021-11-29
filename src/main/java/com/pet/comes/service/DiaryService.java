package com.pet.comes.service;


import com.pet.comes.dto.Rep.DiaryListRepDto;
import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.PinReqDto;
import com.pet.comes.model.Entity.Diary;
import com.pet.comes.model.Entity.Dog;

import com.pet.comes.model.Entity.Pin;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.PinRepository;
import com.pet.comes.repository.UserRepository;
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
import java.util.Optional;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final PinRepository pinRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public DiaryService(PinRepository pinRepository,DogRepository dogRepository,UserRepository userRepository, DiaryRepository diaryRepository, Status status, ResponseMessage message) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.dogRepository = dogRepository;
        this.pinRepository  = pinRepository;
        this.status = status;
        this.message = message;
    }

    /* 다이어리 조회 API -- Tony */
    public ResponseEntity dogDiaryList(Long dogId) {

        List<Diary> diaryList = diaryRepository.findAllByDogId(dogId);
        if (diaryList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARIES), HttpStatus.OK);


        List<DiaryListRepDto> diaryListRepDtoList = new ArrayList<>();
        String createdAt, text;
        int commentCount, pinCount;

        for (Diary diary : diaryList) {
            createdAt = diary.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            text = diary.getText();
            commentCount = 12; // 임의의 값 : 아직 comment 관련 api 만들지않아서
            pinCount = 2; // 임의의 값 : 아직 pin 관련 api 만들지 않아서
            DiaryListRepDto diaryListRepDto = new DiaryListRepDto(createdAt, text, commentCount, pinCount);
            diaryListRepDtoList.add(diaryListRepDto);
        }
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리들 불어오기 " + message.SUCCESS, diaryListRepDtoList), HttpStatus.OK);

    }

    /* 다이어리 작성 API -- Tony */
    public ResponseEntity writeDiary(DiaryReqDto diaryReqDto) {
        if (diaryReqDto.getText() == null)
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + ": 내용이 없습니다."), HttpStatus.OK);
        Optional<User> user = userRepository.findById(diaryReqDto.getUserId());
        if (!user.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 유저가 없습니다."), HttpStatus.OK);

        Long dogId = diaryReqDto.getDogId();
        Optional<Dog> dog = dogRepository.findById(dogId);

        if(!dog.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        if (!user.get().getFamily().getDogs().contains(dog.get()))
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        Diary diary = new Diary(diaryReqDto);
        diary.setUser(user.get());
        diaryRepository.save(diary);
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS, diary.getId()), HttpStatus.OK);
    }

    /* 다이어리 수정 API -- Tony */
    public ResponseEntity modifyDiary(Long diaryId, DiaryReqDto diaryReqDto) {
        Optional<Diary> tmpDiary = diaryRepository.findById(diaryId);

        if (!tmpDiary.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "수정할 " + message.NO_DIARY), HttpStatus.OK);
        } else if (diaryReqDto.getText() == null)
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, "다이어리들 불어오기 " + message.NOT_ENTERED + " : 반려견에게 어떤 일이 있었는지 작성해주세요 !"), HttpStatus.OK);

        tmpDiary.get().modify(diaryReqDto);

        diaryRepository.save(tmpDiary.get());

        return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS + ": 다이어리 수정", diaryId), HttpStatus.OK);

    }

    /* 다이어리 삭제 API -- Tony */
    public ResponseEntity deleteDiary(Long diaryId) {
        Optional<Diary> tmpDiary = diaryRepository.findById(diaryId);
        if (!tmpDiary.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "삭제할 " + message.NO_DIARY), HttpStatus.OK);
        }
        diaryRepository.delete(tmpDiary.get());

        return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS + ": 다이어리 삭제", diaryId), HttpStatus.OK);

    }

    /* 다이어리 공유 설정 변경 API -- Tony */
    public ResponseEntity toggleDiary(Long diaryId) {
        Optional<Diary> tmpDiary = diaryRepository.findById(diaryId);
        if (!tmpDiary.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "삭제할 " + message.NO_DIARY), HttpStatus.OK);
        }
        Diary diary = tmpDiary.get();
        int isPublic = diary.getIsPublic();

        if (isPublic == 1) {
            diary.toggle(0);
        } else if (isPublic == 0)
            diary.toggle(1);
        else
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, "토글실패 : 1 또는 0 이외의 값 "), HttpStatus.OK);

        diaryRepository.save(diary);

        return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS + ": 공개/비공개 토글 성공 : " + isPublic, isPublic), HttpStatus.OK);

    }

    /* 다이어리 핀 설정하기/해제하기 API -- Tony */
    public ResponseEntity pinDiary(PinReqDto pinReqDto){
        Optional<User> isExist = userRepository.findById(pinReqDto.getUserId());
        if(!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID,  message.NOT_VALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

        User user = isExist.get();
        List<Pin> userPinList = user.getPins();
        Long diaryId = pinReqDto.getDiaryId();
        // pin 으로 등록되어 있을 때
        Optional<Pin> isExist2 = pinRepository.findByUserAndDiaryId(user,diaryId);
        if(isExist2.isPresent() ) { // 이미 등록 되어 있다면
            Pin pin = isExist2.get();
            userPinList.remove(pin); // user에서 삭제 (양뱡향)
            pinRepository.delete(pin); // DB에서 삭제
            userRepository.save(user);

            return new ResponseEntity(NoDataResponse.response(status.SUCCESS,  message.SUCCESS + " : 핀 하기 해제"), HttpStatus.OK);
        }

        // pin 으로 등록되어 있지 않을 때
        Pin pin = new Pin(user, diaryId);  // 핀 생성 ( pin -> user 관계 )
        userPinList.add(pin); // 핀 추가  ( user -> pin 관계 )

        pinRepository.save(pin);
        userRepository.save(user);


        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 핀 하기 " ), HttpStatus.OK);


    }

}
