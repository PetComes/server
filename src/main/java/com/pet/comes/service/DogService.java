package com.pet.comes.service;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.FamilyRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class DogService {

    private final DogRepository dogRepository;
    private final Status status;
    private final ResponseMessage message;
    private final UserService userService;
    private final FamilyService familyService;

    @Autowired
    public DogService(UserService userService, FamilyService familyService, DogRepository dogRepository, Status status, ResponseMessage message) {
        this.dogRepository = dogRepository;
        this.familyService = familyService;
        this.userService = userService;
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
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_FAMILY+" : 강아지 등록해보기"), HttpStatus.OK);


        Dog dog = new Dog(userId, dogReqDto);
        dog.setFamily(family);// dog -> family 관계 매핑
        family.setDogs(dog);// family -> dog 관계 매핑
        familyService.addFamily(family);
        userService.setFamilyId(userId, family); // User -> Family 관계 매핑

        dogRepository.save(dog);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }

//    /* H5 : 강아지별 프로필 조회 API -- Tony*/
//    public ResponseEntity getDogProfile(Long dogId) {
//
//    }

}
