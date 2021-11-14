package com.pet.comes.service;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.FamilyRepository;
import com.pet.comes.repository.UserRepository;
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
    private final FamilyRepository familyRepository;
    private final UserService userService;

    @Autowired
    public DogService(UserService userService, FamilyRepository familyRepository,DogRepository dogRepository,Status status, ResponseMessage message){
        this.dogRepository = dogRepository;
        this.familyRepository = familyRepository;
        this.userService =userService;
        this.status = status;
        this.message = message;
    }



    public ResponseEntity addDog(Long userId,DogReqDto dogReqDto ){
        if(dogReqDto.getName() == null  || dogReqDto.getAge() > 25 ){

            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);

        }


        Family family = new Family(); // dog 생성시 family 객체 생성

        Dog dog =  new Dog(dogReqDto,family); // dog -> family 관계 매핑
        family.setDogs(dog); // family -> dog 관계 매핑

        familyRepository.save(family); // dog <-> family 관계 매핑 끝난 family 객체 DB 반영
        userService.setFamilyId(userId, family); // User -> Family 관계 매핑


        dogRepository.save(dog);



        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }



}
