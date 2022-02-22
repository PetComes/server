package com.pet.comes.service;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.dto.Rep.*;
import com.pet.comes.model.Entity.*;
import com.pet.comes.repository.*;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Status status;
    private final ResponseMessage message;
    private final DiaryRepository diaryRepository;
    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final PinRepository pinRepository;
    private final DogRepository dogRepository;
    private final ModelMapper modelMapper;

    @Transactional // 해당 메소드가 호출될 때 바뀐 내용을 DB에 반영
    public Long setFamilyId(Long id, Family family) {
        User user = userRepository.findById(id).orElseThrow( // 예외처리 : 만약에 없다면 ?
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.") // 파라미터가 잘 못 들어왔을 때
        );

        user.setFamilyId(family);
        userRepository.save(user);
        return user.getId();
    }

    /* 강아지 등록 API : 해당 User family 유무 확인--Tony */
    public Family userFamily(Long userId) {
        Family family = userRepository.findById(userId).get().getFamily();
        if (family == null) {
            family = new Family(); // new ~ 는 비영속 : 엔티티 객체를 생성했지만 아직 영속성 컨텍스트에 저장하지 않은 상태
        }
        return family;
    }

    /* 회원가입 API -- Heather */
    public ResponseEntity signUp(UserJoinDto userJoinDto) {

        //name, email, nickname non-null
        if (userJoinDto.getName() == null || userJoinDto.getEmail() == null || userJoinDto.getNickname() == null) {
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);
        }

        User user = new User(userJoinDto);
        Optional<User> isExist = userRepository.findByEmail(userJoinDto.getEmail());
        if (isExist.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.DUPLICATED_EMAIL,
                    user.getEmail() + " : " + message.DUPLICATED_EMAIL), HttpStatus.OK);
        }

        isExist = userRepository.findByNickname(userJoinDto.getNickname());
        if (isExist.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.DUPLICATED_NICKNAME,
                    user.getEmail() + " : " + message.DUPLICATED_NICKNAME), HttpStatus.OK);
        }

        userRepository.save(user);
        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "회원가입 " + message.SUCCESS, user.getId()), HttpStatus.OK);

    }

    /* H1 : 내 계정정보 조회 API -- Tony */
    public ResponseEntity myAccount(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            MyAccountRepDto myAccountRepDto = new MyAccountRepDto(user.get());
            return new ResponseEntity(DataResponse.response(
                    200, "내 계정정보 조회 " + new ResponseMessage().SUCCESS, myAccountRepDto
            ), HttpStatus.OK);
        }
        return new ResponseEntity(NoDataResponse.response(
                404, new ResponseMessage().INVALID_ACCOUNT
        ), HttpStatus.NOT_FOUND);

    }

    /* H2 : 읽지 않은 알림 개수 조회 --Tony */
    public ResponseEntity alarmCount(Long userId) {
        Optional<User> isExist = userRepository.findById(userId);
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);

        User user = isExist.get();
        List<Alarm> alarmList = alarmRepository.findAllByUserAndNotChecked(user, 0); // 아직 체크 안된 알림 갯수 알기 위해

        if (alarmList.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_ALARMS), HttpStatus.OK);

        int cnt = alarmList.size();

        return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS, cnt), HttpStatus.OK);
    }

    /* H3 : 알림 목록 조회 --Tony */
    public ResponseEntity showAlarmList(Long userId) {

        // User 유효성 검사
        Optional<User> isExistUser = userRepository.findById(userId);
        if (!isExistUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);

        User user = isExistUser.get(); // 계정 주인 user 객체

        // 해당 유저에대한 Alarm 다 가져오기
        List<Alarm> alarmList = alarmRepository.findAllByUser(user);  // 알람 전체 찾아오기
        String imageurl = "";
        String nickname = "";
        String messageStr = "";

        List<AlarmListRepDto> alarmListRepDtoList = new ArrayList<>();

        // return 해줄 dto로 변경
        for (Alarm alarm : alarmList) {

            // 다이어리 댓글인지 핀인지 확인
            if (alarm.getType() == 1) { // 1: 댓글
                Optional<Comment> isExist = commentRepository.findById(alarm.getContentId()); // type == 1 일때 content_id == comment_id

                if (!isExist.isPresent())  // 댓글이 없어졌을 때
                    messageStr = "댓글이 삭제되거나 게시물이 없습니다.";
                else { // 댓글이 존재할 때
                    // comment -> user 정보 찾기
                    Comment comment = isExist.get(); // 댓글 가져오기
                    User usertmp = comment.getUser(); // 해당 댓글을 작성한 유저
                    imageurl = usertmp.getImageUrl();
                    nickname = usertmp.getNickname();
                    messageStr = nickname + "님이 회원님의 게시글에 댓글을 달았습니다.";
                }

                // return 해줄 List에 추가하기
                AlarmListRepDto alarmListRepDto = new AlarmListRepDto(alarm.getDiary().getId(), imageurl, nickname, messageStr, alarm.getCreatedAt());
                alarmListRepDtoList.add(alarmListRepDto);

            } else if (alarm.getType() == 0) { // 0 : 핀
                Optional<User> isUserExist = userRepository.findById(alarm.getContentId());// user_id로 pin 조회
                if (!isUserExist.isPresent()) // 해당 유저가 탈퇴할 경우
                    messageStr = "존재하지 않는 계정입니다.";
                User userParam = isUserExist.get();

                Optional<Pin> isExist = pinRepository.findByUserAndDiaryId(userParam, alarm.getDiary().getId()); // 핀을 영속성컨텍스트에서 찾기
                if (!isExist.isPresent())  // 핀이 db상에서 오류 났을 때 ?
                    messageStr = "핀한 다이어리가 존재하지 않습니다.";

                else {
                    Pin pin = isExist.get();
                    User usertmp = pin.getUser(); // 해당 핀 객체를 가진 유저
                    imageurl = usertmp.getImageUrl();
                    nickname = usertmp.getNickname();
                    messageStr = nickname + "님이 회원님의 게시글을 핀했습니다.";
                }
                // return 해줄 List에 추가하기
                AlarmListRepDto alarmListRepDto = new AlarmListRepDto(alarm.getDiary().getId(), imageurl, nickname, messageStr, alarm.getCreatedAt());
                alarmListRepDtoList.add(alarmListRepDto);
            }
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS, message.SUCCESS, alarmListRepDtoList), HttpStatus.OK);
    }

    /* H6 : 내 핀 목록 조회 API -- Tony */
    public ResponseEntity pinList(Long userId) {
        Optional<User> isExist = userRepository.findById(userId);

        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

        User user = isExist.get(); // 해당 api 요청한 user의 id

        List<Pin> pinList = user.getPins();
        List<PinListRepDto> pinListRepDtoList = new ArrayList<>();
        PinListRepDto pinListRepDto = new PinListRepDto();

        for (Pin pin : pinList) {
            Optional<Diary> isExist2 = diaryRepository.findById(pin.getDiaryId());
            if (!isExist2.isPresent())
                return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT + "유저 다이어리가 없습니다. "), HttpStatus.OK);
            Diary diary = isExist2.get();
            pinListRepDto.setText(diary.getText());
            pinListRepDto.setContentImageUrl(diary.getDiaryImgUrl());

            User usertmp = diary.getUser();
            if (usertmp == null)
                return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT + "유저 정보가 없습니다. "), HttpStatus.OK);

            pinListRepDto.setNickname(usertmp.getNickname());
            pinListRepDto.setProfileImageUrl(usertmp.getImageUrl());

            pinListRepDtoList.add(pinListRepDto);
        }

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                message.SUCCESS + " 핀 목록 조회 ", pinListRepDtoList), HttpStatus.OK);

    }


    /* H7 : 내 가족 목록 조회 API -- Tony */
    public ResponseEntity myFamily(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            Family family = user.get().getFamily();
            if (family != null) {
//            int familyId = tmpfamilyId.intValue();
                List<User> myfamilys = userRepository.findAllByFamily(family);
                List<MyFamilyRepDto> myfamilyRepDtos = new ArrayList<>();

                for (User tmpUser : myfamilys) {
                    MyFamilyRepDto familyDto = new MyFamilyRepDto(tmpUser);
                    myfamilyRepDtos.add(familyDto);
                }

                return new ResponseEntity(DataResponse.response(
                        200, "내 가족 목록 조회 " + new ResponseMessage().SUCCESS, myfamilyRepDtos
                ), HttpStatus.OK);
            }
        }

        return new ResponseEntity(NoDataResponse.response(
                404, "내 가족 목록 조회 : " + new ResponseMessage().NO_FAMILY
        ), HttpStatus.NOT_FOUND);
    }

    /* H11 : 알림확인(체크하기) -- Tony */
    public ResponseEntity checkAlarm(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalAccessError("잘못된 userId입니다."));

        List<Alarm> allByUser = alarmRepository.findAllByUser(user);

        if (allByUser.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.DB_NO_DATA, message.NO_ALARMS), HttpStatus.NOT_FOUND);

        for (Alarm alarm : allByUser) {
            alarm.setIsChecked(1);
        }

        alarmRepository.saveAll(allByUser);


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, "알림체크 " + message.SUCCESS, user.getId()
        ), HttpStatus.OK);

    }

    /* U5 : 닉네임 중복여부 확인(유저 닉네임) -- Tony */
    public ResponseEntity validNickname(String nickname) {
        Optional<User> isExist = userRepository.findByNickname(nickname);

        if (isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.EXISTED_NICKNAME
                    , new ResponseMessage().EXISTED_NICKNAME
            ), HttpStatus.NOT_FOUND);


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, nickname + " 닉네임  " + new ResponseMessage().SUCCESS, nickname
        ), HttpStatus.OK);

    }

    /* S9 : 클릭한 계정 프로필 확인하기 --Tony */
    public ResponseEntity getUserProfile(@PathVariable String userName) {

        Optional<User> isExist = userRepository.findByNickname(userName);
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID
                    , new ResponseMessage().INVALID_ACCOUNT
            ), HttpStatus.NOT_FOUND);

        User user = isExist.get();

//        List<GetProfileRepDto> getProfileRepDtoList = new ArrayList<>();

        // 뱃지 연관관계 결정되면 넣기
        // 유저관련 정보들
        GetProfileRepDto getProfileRepDto = new GetProfileRepDto(user.getNickname(), user.getImageUrl(), user.getIntroduction(), "뱃지관련은 작업후 추가");

        // 반환할 결과 리스트에 추가
//        getProfileRepDtoList.add(getProfileRepDto);

        Family family = user.getFamily();
        if (family == null) // 반려견이 없을 때 (최초 생성 하지 않으면 family 없음)
            return new ResponseEntity(DataResponse.response(
                    status.SUCCESS, new ResponseMessage().SUCCESS + " 반려견을 등록하지 않음.", getProfileRepDto
            ), HttpStatus.OK);

        // 반려견 관련 정보들
        List<Dog> dogList = dogRepository.findAllByFamily(family);
        if (dogList.isEmpty()) // 반려견이 없을 때
            return new ResponseEntity(DataResponse.response(
                    status.SUCCESS, new ResponseMessage().SUCCESS + " 반려견을 등록하지 않음.", getProfileRepDto
            ), HttpStatus.OK);

        // 반려견이 있을 때 -> 반려견 이름, 반려견 사진
        List<DogsProfileRepDto> params = new ArrayList<>();
        for (Dog dog : dogList) {
            // 반려견 관련 정보들
            DogsProfileRepDto dto = new DogsProfileRepDto(dog.getName(), dog.getImageUrl());
            params.add(dto);
            getProfileRepDto.setGetDogsProfileRepDtoList(params);

        }


        return new ResponseEntity(DataResponse.response(
                status.SUCCESS, new ResponseMessage().SUCCESS + "유저, 반려견 모두 등록", getProfileRepDto
        ), HttpStatus.OK);

    }


}
