package com.pet.comes.service;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.dto.Rep.MyAccountRepDto;
import com.pet.comes.dto.Rep.MyFamilyRepDto;
import com.pet.comes.dto.Rep.UserProfileRepDto;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public UserService(UserRepository userRepository, FamilyService familyService, Status status, ResponseMessage message) {
        this.userRepository = userRepository;
        this.status = status;
        this.message = message;
    }


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
                404, new ResponseMessage().NOT_VALID_ACCOUNT
        ), HttpStatus.NOT_FOUND);

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

    /* S10 : 클릭한 계정 프로필 확인하기 --Tony */
    public ResponseEntity getUserProfile(@PathVariable String userName) {
        Optional<User> isExist = userRepository.findByNickname(userName);
        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID
                    , new ResponseMessage().NOT_VALID_ACCOUNT
            ), HttpStatus.NOT_FOUND);

        User user = isExist.get();
        // 뱃지 연관관계 결정되면 넣기
        UserProfileRepDto userProfileRepDto;

        return new ResponseEntity(NoDataResponse.response(status.INVALID_ID
                , new ResponseMessage().NOT_VALID_ACCOUNT
        ), HttpStatus.NOT_FOUND);

    }


}
