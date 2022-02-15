package com.pet.comes.service;


import com.pet.comes.dto.Rep.DiaryListRepDto;
import com.pet.comes.dto.Rep.DiaryUserDto;
import com.pet.comes.dto.Rep.PinListofDiaryDto;
import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.PinReqDto;
import com.pet.comes.model.Entity.*;

import com.pet.comes.model.EnumType.SortedType;
import com.pet.comes.repository.*;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;
    private final AddressRepository addressRepository;
    private final AlarmRepository alarmRepository;
    private final Status status;
    private final ResponseMessage message;
    private final ModelMapper modelMapper;

    /* D1 : 강아지별 다이어리 조회 API -- Tony */
    public ResponseEntity dogDiaryList(Long userId, String nickName, String dogName) {

        // 조회하고자하는 강아지의 견주 닉네임으로 유저 찾기
        Optional<User> isUser = userRepository.findByNickname(nickName);
        if (!isUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);

        User user = isUser.get();
        Family family = user.getFamily();
        if (family == null) // 아직 가족을 생성하지 않으면
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_FAMILY), HttpStatus.OK);

        List<Dog> dogs = family.getDogs();
        if (dogs.isEmpty()) // 반려견이 없다면
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, message.NO_DOG), HttpStatus.OK);

        Long dogId = 0L; // 초기화 0 : dogId는 1부터 시작함.
        for (Dog dog : dogs) {
            if (dog.getName().equals(dogName))
                dogId = dog.getId();
        }

        List<Diary> diaryList = diaryRepository.findAllByDogId(dogId);
        if (diaryList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DIARIES), HttpStatus.OK);


        List<DiaryListRepDto> diaryListRepDtoList = new ArrayList<>();
        String createdAt, text;
        int commentCount, pinCount, isPublic;

        if (userId == user.getId()) {// 유저 본인이 자신의 다이어리 조회할 때
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
                isPublic = diary.getIsPublic();
                DiaryListRepDto diaryListRepDto = new DiaryListRepDto(createdAt, text, commentCount, pinCount, isPublic, diary.getId());
                diaryListRepDtoList.add(diaryListRepDto);

            }
        } else { // 다른 유저가 다이어리를 조회할 때
            for (Diary diary : diaryList) {
                if (diary.getIsPublic() == 1) { // 공개인 다이어리만 보여주기
                    createdAt = diary.getRegisteredAt().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    text = diary.getText();
                    commentCount = diary.getHowManyComments();
                    pinCount = diary.getHowManyPins();
                    isPublic = diary.getIsPublic();
                    DiaryListRepDto diaryListRepDto = new DiaryListRepDto(createdAt, text, commentCount, pinCount, isPublic, diary.getId());
                    diaryListRepDtoList.add(diaryListRepDto);
                }
            }
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리들 불어오기 " + message.SUCCESS, diaryListRepDtoList), HttpStatus.OK);

    }

    /* D2 :다이어리 작성 API -- Tony */
    public ResponseEntity writeDiary(DiaryReqDto diaryReqDto) {
        if (diaryReqDto.getUserId() == null) // userId 가 body에 없음
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + ": userId가 없습니다. body에 userId를 넣어주세요."), HttpStatus.OK);

        if (diaryReqDto.getText() == null) // Text data 자체가 없음
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + ": 내용이 없습니다. Text를 작성해 주세요(2글자 이상)"), HttpStatus.OK);
        if (diaryReqDto.getText().length() < 2) // Text는 최소 글자 이상
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED + " Text를 2글자 이상 작성해주세요. "), HttpStatus.OK);

        // diary <-> user 다대일 양방향 매핑을 위해
        Optional<User> user = userRepository.findById(diaryReqDto.getUserId());
        if (!user.isPresent()) //
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.INVALID_ACCOUNT + ": 해당 유저가 없습니다."), HttpStatus.OK);

        //
        Long dogId = diaryReqDto.getDogId();
        Optional<Dog> dog = dogRepository.findById(dogId);

        if (!dog.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.INVALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        if (!user.get().getFamily().getDogs().contains(dog.get()))
            return new ResponseEntity(NoDataResponse.response(status.DB_INVALID_VALUE, message.INVALID_ACCOUNT + ": 해당 반려견이 없습니다."), HttpStatus.OK);

        Diary diary = new Diary(diaryReqDto); // 이 시점에서는 비영속 상태  , connection pool을 가져오지 않는다다        diary.setUser(user.get()); // diary <-> user 다대일 양방향 매핑

        if (diaryReqDto.getLocationName() == null) { // 위치 정보 없을 때

            diaryRepository.save(diary); // Tranaction 발생 -> Persistence Context 와 직접적인 관련이 생김.
            return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS + " 위치태그 없음.", diary.getId()), HttpStatus.OK);
        }

        /*--위치 정보 있을 때 --*/
        Address address = new Address(diaryReqDto); // 위치 객체 생성

        // db반영 : Entity 생성 완료 ( 영속성 컨텍스트에 생성됨 )
        diaryRepository.save(diary);
        addressRepository.save(address);

        // diary <-> address ,OneToOne 양뱡향 매핑
        address.setDiary(diary);
        diary.setAddress(address);

        // db반영 : 변경사항 다시 저장
        diaryRepository.save(diary);
        addressRepository.save(address);


        return new ResponseEntity(DataResponse.response(status.SUCCESS, "다이어리 작성 " + message.SUCCESS + " 위치태그 존재", diary.getId()), HttpStatus.OK);
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

        List<PinListofDiaryDto> resultList = new ArrayList<>();

        for (Pin pin : pinList) {
            User user = userRepository.findById(pin.getUser().getId()).get();
            String imgurl = user.getImageUrl();
            String nickName = user.getName();
            PinListofDiaryDto pinListofDiaryDto = new PinListofDiaryDto(imgurl, nickName);
            resultList.add(pinListofDiaryDto);
//            cnt ++;
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                message.SUCCESS + " 다이어리의 핀목록 상세 조회 ", resultList), HttpStatus.OK);
    }

    /* 다이어리 핀 설정하기/해제하기 API -- Tony */
    public ResponseEntity pinDiary(PinReqDto pinReqDto) {

        System.out.println("start test ==========");
        Optional<User> isExist = userRepository.findById(pinReqDto.getUserId());
        // 유저 유효성 검사
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

        User user = isExist.get(); // 유저 객체 가져와서
        List<Pin> userPinList = user.getPins(); // 유저객체가 참조하고 있는 핀들 가져오기
        Long diaryId = pinReqDto.getDiaryId(); // 해당 유저가 핀 설정/해제할 다이어리 id
        Optional<Diary> isDiary = diaryRepository.findById(diaryId);

        // 다이어리 유효성 검사
        if (!isDiary.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID
                    , new ResponseMessage().NO_DIARY
            ), HttpStatus.NOT_FOUND);

        Diary diary = isDiary.get(); // 다이어리 객체 가져오기
        int howManypins = diary.getHowManyPins();


        // pin 으로 이미 등록되어 있을 때 -> 핀 해제하기
        Optional<Pin> isExist2 = pinRepository.findByUserAndDiaryId(user, diary.getId());
        if (isExist2.isPresent()) { // 이미 등록 되어 있다면
            Pin pin = isExist2.get();
            userPinList.remove(pin); // user에서 삭제 (양뱡향) : 핀 해제
            pinRepository.delete(pin); // DB에서  Pin 삭제
            diary.setHowManyPins(howManypins - 1); // pin 카운트 하나 삭제
            userRepository.save(user); // db 반영
            diaryRepository.save(diary); // db 반영
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 핀 하기 해제"), HttpStatus.OK);
        }

        // pin 으로 등록되어 있지 않을 때
        Pin pin = new Pin(user, diaryId);  // 핀 생성 ( pin -> user 관계  )
        userPinList.add(pin); // 핀 추가  ( user -> pin 관계 )

        pinRepository.save(pin); // DB에 pin 추가
        diary.setHowManyPins(howManypins + 1); // pin 카운트 하나 증가
        userRepository.save(user); // db반영
        int type = 0;
        System.out.println("===== before Query =====");
        List<Alarm> isExistAlarm = alarmRepository.findAllByUserAndTypeAndContendId(user, diary, type);
        System.out.println("===== Query findAllByUserAndTypeANdContendId Success =====");
        System.out.println("size : " + isExistAlarm.size());
        if (isExistAlarm.size() == 0) { // 해당 다이어리에대한 핀하기를 똑같은 유저가 비중복적으로 누를때만 db에 알람 만들어주기
            System.out.println("==== 중복알람 x, 알람 만들기 ====");
            Alarm alarm = new Alarm(diary.getUser(), 0, 0, user.getId(), diary); // 해당 다이어리의 주인 ,type = 0 : 핀하기 / isChecked = 0 : 읽지 않음
            alarmRepository.save(alarm);
            System.out.println("==== 알람만들기 success ==== ");
        }

        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, message.SUCCESS + " : 핀 하기 "), HttpStatus.OK);


    }

    /* S1 인기순, 최신순 다이어리 정렬, 조회 -- Tony */
    public ResponseEntity getAllDiary(SortedType sortedType) {
//        List<Diary> diaryList = diaryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
//          List<Diary> diaries = diaryRepository.findAllByIsPublicOrderByIdDesc(1);

//        List<DiaryAllListRepDto> diaryListRepDtoList = diaryRepository.findAllByIsPublicOrderByIdDesc(1)//findAll(Sort.by(Sort.Direction.DESC,"id"))
//                .stream()
//                .map(diary -> modelMapper.map(diary, DiaryAllListRepDto.class))
//                .collect(Collectors.toList());
        if (sortedType.equals(SortedType.CURRENT)) {
            List<IDiaryUserDto> allByIsPublicOrderByIdDescQuery = diaryRepository.findAllByIsPublicOrderByIdDescQuery()
                    .stream()
                    .map(d -> modelMapper.map(d, IDiaryUserDto.class))
                    .collect(Collectors.toList());

            if (allByIsPublicOrderByIdDescQuery.isEmpty())
                return new ResponseEntity(NoDataResponse.response(status.DB_NO_DATA, message.NO_COMMUNITY_CONTENTS), HttpStatus.OK);

            return new ResponseEntity(DataResponse.response(status.SUCCESS, " 공유다이어리 게시물 조회 (최신순) " + message.SUCCESS, allByIsPublicOrderByIdDescQuery), HttpStatus.OK);

        } else if (sortedType.equals(SortedType.BEST)) {
            List<IDiaryUserDto> allByIsPublicOrderByIdDescQuery = diaryRepository.findAllByIsPublicOrderByHowManyPinsDescQuery()
                    .stream()
                    .map(d -> modelMapper.map(d, IDiaryUserDto.class))
                    .collect(Collectors.toList());

            if (allByIsPublicOrderByIdDescQuery.isEmpty())
                return new ResponseEntity(NoDataResponse.response(status.DB_NO_DATA, message.NO_COMMUNITY_CONTENTS), HttpStatus.OK);

            return new ResponseEntity(DataResponse.response(status.SUCCESS, " 공유다이어리 게시물 조회 (인기순) " + message.SUCCESS, allByIsPublicOrderByIdDescQuery), HttpStatus.OK);

        }
//        if(!isDiary.isPresent())
//            return new ResponseEntity(NoDataResponse.response(status.DB_NO_DATA, message.NO_DIARY  ), HttpStatus.OK);

        return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_ENTERED + " 파라미터에 BEST, CURRENT를 입력해주세요."), HttpStatus.OK);

    }

}
