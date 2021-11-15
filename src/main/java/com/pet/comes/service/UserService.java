package com.pet.comes.service;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.FamilyRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public UserService(FamilyRepository familyRepository, UserRepository userRepository, Status status, ResponseMessage message) {
        this.userRepository = userRepository;
        this.familyRepository = familyRepository;
        this.status = status;
        this.message = message;
    }


    /* 강아지 등록 API : User -> family 연관 매핑 -- Tony */
    @Transactional // 해당 메소드가 호출될 때 바뀐 내용을 DB에 반영
    public Family setFamilyId(Long userId,Family family) {
        User user = userRepository.findById(userId).orElseThrow( // 예외처리 : 만약에 없다면 ?
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.") // 파라미터가 잘 못 들어왔을 때
        );



        user.setFamilyId(family); // family_id 주입
//        familyRepository.save(family); // dog <-> family 관계 매핑 끝난 family 객체 DB 반영
        userRepository.save(user); // user -> family 관계 매핑 끝난 user 객체 DB 반영
        return user.getFamily();


    }


    public Optional<User> myAccount(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
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


}
