package com.pet.comes.service;


import com.pet.comes.dto.Rep.DiaryListRepDto;
import com.pet.comes.dto.Rep.PinListRepDto;
import com.pet.comes.dto.Rep.PinListofDiaryDto;
import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.PinReqDto;
import com.pet.comes.model.Entity.*;

import com.pet.comes.repository.*;
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
    private final CommentRepository commentRepository;
    private  final AddressRepository addressRepository;
    private final Status status;
    private final ResponseMessage message;


    @Autowired
    public DiaryService(AddressRepository addressRepository,CommentRepository commentRepository,PinRepository pinRepository, DogRepository dogRepository, UserRepository userRepository, DiaryRepository diaryRepository, Status status, ResponseMessage message) {
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        this.dogRepository = dogRepository;
        this.pinRepository = pinRepository;
        this.commentRepository = commentRepository;
        this.addressRepository = addressRepository;
        this.status = status;
        this.message = message;
    }

    /* D1 : 강아지별 다이어리 조회 API -- Tony */
    public ResponseEntity dogDiaryList(String nickName, String dogName) {

        // 조회하고자하는 강아지의 견주 닉네임으로 유저 찾기
        Optional<User> isUser = userRepository.findByNickname(nickName);
        if(!isUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT), HttpStatus.OK);

        User user = isUser.get();
        Family family = user.getFamily();
        if(family == null) // 아직 가족을 생성하지 않으면
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_FAMILY), HttpStatus.OK);

        List<Dog> dogs = family.getDogs();
        if(dogs.isEmpty()) // 반려견이 없다면
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, message.NO_DOG), HttpStatus.OK);

        Long dogId = 0L; // 초기화 0 : dogId는 1부터 시작함.
        for(Dog dog : dogs){
            if(dog.getName().equals(dogName))
                dogId =dog.getId();
        }

        List<Diary> diaryList = diaryRepository.findAllByDogId(dogId);
        if (diaryList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARIES), HttpStatus.OK);


        List<DiaryListRepDto> diaryListRepDtoList = new ArrayList<>();
        String createdAt, text;
        int commentCount , pinCount;


        for (Diary diary : diaryList) {
            createdAt = diary.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            text = diary.getText();
//            List<Comment> comments = commentRepository.findAllByDiary(diary); // 임의의 값 : 아직 comment 관련 api 만들지않아서
//            for(Comment comment : comments){
//                if(!(comment==null))
//                    commentCount++;
//            }
            commentCount = diary.getHowManyComments();
            pinCount = diary.getHowManyPins();
            DiaryListRepDto diaryListRepDto = new DiaryListRepDto(createdAt, text, commentCount, pinCount, diary.getId());
            diaryListRepDtoList.add(diaryListRepDto);
        }
        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리들 불어오기 " + message.SUCCESS, diaryListRepDtoList), HttpStatus.OK);

    }

    /* 다이어리 작성 API -- Tony */
    public ResponseEntity writeDiary(DiaryReqDto diaryReqDto) {
        if (diaryReqDto.getText() == null ) // Text data 자체가 없음
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + ": 내용이 없습니다. Text를 작성해 주세요(2글자 이상)"), HttpStatus.OK);
        if (diaryReqDto.getText().length() < 2 ) // Text는 최소 글자 이상
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + " Text를 2글자 이상 작성해주세요. "), HttpStatus.OK);

        Optional<User> user = userRepository.findById(diaryReqDto.getUserId());
        if (!user.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 유저가 없습니다."), HttpStatus.OK);

        Long dogId = diaryReqDto.getDogId();
        Optional<Dog> dog = dogRepository.findById(dogId);

        if (!dog.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        if (!user.get().getFamily().getDogs().contains(dog.get()))
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.NOT_VALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        Diary diary = new Diary(diaryReqDto);
        diary.setUser(user.get());

        if(diaryReqDto.getLocationName() == null) { // 위치 정보 없을 때

            diaryRepository.save(diary);
            return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS+" 위치태그 없음.", diary.getId()), HttpStatus.OK);
        }

        /*--위치 정보 있을 때 --*/
        Address address = new Address(diaryReqDto); // 위치 객체 생성

        // db반영 : Entity 생성 완료 ( 영속성 컨텍스트에 생성됨 )
        diaryRepository.save(diary);
        addressRepository.save(address);

        // OneToOne 양뱡향 매핑
        address.setDiary(diary);
        diary.setAddress(address);

        // db반영 : 변경사항 다시 저장
        diaryRepository.save(diary);
        addressRepository.save(address);



        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS+" 위치태그 존재", diary.getId()), HttpStatus.OK);
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

    /* D7 다이어리 핀한수 상세보기 API -- Tony */
    public ResponseEntity getPinListofDiary(Long diaryId) {
        List<Pin> pinList = pinRepository.findAllByDiaryId(diaryId);

        if (pinList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARY), HttpStatus.OK);
//        int cnt = 0; // 전체핀 수 카운트하기

        List<PinListofDiaryDto> resultList =  new ArrayList<>();

        for(Pin pin : pinList){
            User user = userRepository.findById(pin.getUser().getId()).get();
            String imgurl = user.getImageUrl();
            String nickName = user.getName();
            PinListofDiaryDto pinListofDiaryDto = new PinListofDiaryDto(imgurl,nickName);
            resultList.add(pinListofDiaryDto);
//            cnt ++;
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                message.SUCCESS + " 다이어리의 핀목록 상세 조회 ", resultList), HttpStatus.OK);
    }

    /* 다이어리 핀 설정하기/해제하기 API -- Tony */
    public ResponseEntity pinDiary(PinReqDto pinReqDto) {
        Optional<User> isExist = userRepository.findById(pinReqDto.getUserId());
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

        User user = isExist.get();
        List<Pin> userPinList = user.getPins();
        Long diaryId = pinReqDto.getDiaryId();
        // pin 으로 등록되어 있을 때
        Optional<Pin> isExist2 = pinRepository.findByUserAndDiaryId(user, diaryId);
        if (isExist2.isPresent()) { // 이미 등록 되어 있다면
            Pin pin = isExist2.get();
            userPinList.remove(pin); // user에서 삭제 (양뱡향)
            pinRepository.delete(pin); // DB에서 삭제
            userRepository.save(user);

            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 핀 하기 해제"), HttpStatus.OK);
        }

        // pin 으로 등록되어 있지 않을 때
        Pin pin = new Pin(user, diaryId);  // 핀 생성 ( pin -> user 관계 )
        userPinList.add(pin); // 핀 추가  ( user -> pin 관계 )

        pinRepository.save(pin);
        userRepository.save(user);


        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 핀 하기 "), HttpStatus.OK);


    }

    /* 내 핀 목록 조회 API -- Tony */
    public ResponseEntity pinList(Long userId) {
        Optional<User> isExist = userRepository.findById(userId);

        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

        User user = isExist.get(); // 해당 api 요청한 user의 id

        List<Pin> pinList = user.getPins();
        List<PinListRepDto> pinListRepDtoList = new ArrayList<>();
        PinListRepDto pinListRepDto = new PinListRepDto();

        for (Pin pin : pinList) {
            Optional<Diary> isExist2 = diaryRepository.findById(pin.getDiaryId());
            if (!isExist2.isPresent())
                return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT + "유저 다이어리가 없습니다. "), HttpStatus.OK);
            Diary diary = isExist2.get();
            pinListRepDto.setText(diary.getText());
            pinListRepDto.setContentImageUrl(diary.getImageUrl());

            User usertmp = diary.getUser();
            if (usertmp == null)
                return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

            pinListRepDto.setNickname(usertmp.getNickname());
            pinListRepDto.setProfileImageUrl(usertmp.getImageUrl());

            pinListRepDtoList.add(pinListRepDto);
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                message.SUCCESS + " 핀 목록 조회 ", pinListRepDtoList), HttpStatus.OK);

    }

}
