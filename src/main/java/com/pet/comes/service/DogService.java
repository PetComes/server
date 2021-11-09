package com.pet.comes.service;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DogService {
    private final DogRepository dogRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public DogService(DogRepository dogRepository, Status status, ResponseMessage message){
        this.dogRepository = dogRepository;
        this.status = status;
        this.message = message;
    }

    public ResponseEntity addDog(DogReqDto dogReqDto ){
        if(dogReqDto.getName() == null  || dogReqDto.getAge() > 20 ){
            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);

        }

        Dog dog = new Dog(dogReqDto);
        //Optional<Dog>


//        Family family = new Family(dog);
        dogRepository.save(dog);
        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }



}
