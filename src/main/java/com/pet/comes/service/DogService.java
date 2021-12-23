package com.pet.comes.service;

import com.pet.comes.dto.Rep.DogProfileRepDto;
import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DogService {

    private final DogRepository dogRepository;
    private final Status status;
    private final ResponseMessage message;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FamilyService familyService;

    @Autowired
    public DogService(UserRepository userRepository, UserService userService, FamilyService familyService, DogRepository dogRepository, Status status, ResponseMessage message) {
        this.dogRepository = dogRepository;
        this.familyService = familyService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.status = status;
        this.message = message;
    }


    /* H4 : 강아지 등록 API --Tony */
    public ResponseEntity addDog(Long userId, DogReqDto dogReqDto) {
        if (dogReqDto.getName() == null || dogReqDto.getAge() > 25) {

            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);

        }

        Family family = userService.userFamily(userId);
        if (family == null)
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_FAMILY + " : 강아지 등록해보기"), HttpStatus.OK);


        Dog dog = new Dog(userId, dogReqDto);
        dog.setFamily(family);// dog -> family 관계 매핑
        family.setDogs(dog);// family -> dog 관계 매핑
        familyService.addFamily(family);
        userService.setFamilyId(userId, family); // User -> Family 관계 매핑

        dogRepository.save(dog);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }

    /* H5 : 강아지별 프로필 조회 API -- Tony*/
    public ResponseEntity getDogProfile(String nickName, String dogName) {
        Optional<User> isUser = userRepository.findByNickname(nickName);
        if (!isUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NOT_VALID_ACCOUNT), HttpStatus.OK);

        User user = isUser.get();
        Family family = user.getFamily();
        if (family == null)
            return new ResponseEntity(NoDataResponse.response(status.INVALID_FAMILYID, message.NO_FAMILY), HttpStatus.OK);

        List<Dog> dogs = family.getDogs();
        if (dogs.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, message.NO_DOG), HttpStatus.OK);

        Long dogId = 0L; // id가 0인 반려견은 없음. 유효성 검사에서 걸리게됨.
        for (Dog dog : dogs) {
            if (dog.getName().equals(dogName)) {
                dogId = dog.getId();
                break;
            }
        }
        Optional<Dog> isExist = dogRepository.findById(dogId);

        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DOG), HttpStatus.OK);

        Dog dog = isExist.get();
        DogProfileRepDto dogProfileRepDto = new DogProfileRepDto(dog);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "강아지별 프로필 조회 " + message.SUCCESS, dogProfileRepDto), HttpStatus.OK);

    }


}
