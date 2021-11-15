package com.pet.comes.service;

import com.pet.comes.dto.Join.UserJoinDto;
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

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public UserService(UserRepository userRepository, Status status, ResponseMessage message) {
        this.userRepository = userRepository;
        this.status = status;
        this.message = message;
    }


    @Transactional // 해당 메소드가 호출될 때 바뀐 내용을 DB에 반영
    public Long setFamilyId(Long id,Family family) {
        User user = userRepository.findById(id).orElseThrow( // 예외처리 : 만약에 없다면 ?
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.") // 파라미터가 잘 못 들어왔을 때
        );
        user.setFamilyId(family);
        userRepository.save(user);
        return user.getId();
    }


    /* 회원가입 API -- Heather */
    public ResponseEntity signUp(UserJoinDto userJoinDto) {

        //name, email, nickname non-null
        if(userJoinDto.getName() == null || userJoinDto.getEmail() == null || userJoinDto.getNickname() == null) {
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);
        }

        User user = new User(userJoinDto);
        Optional<User> isExist = userRepository.findByEmail(userJoinDto.getEmail());
        if(isExist.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.DUPLICATED_EMAIL,
                    user.getEmail() + " : " + message.DUPLICATED_EMAIL), HttpStatus.OK);
        }

        isExist = userRepository.findByNickname(userJoinDto.getNickname());
        if(isExist.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.DUPLICATED_NICKNAME,
                    user.getEmail() + " : " + message.DUPLICATED_NICKNAME), HttpStatus.OK);
        }

        userRepository.save(user);
        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "회원가입 " + message.SUCCESS, user.getId()), HttpStatus.OK);

    }


}
