package com.pet.comes.service;

import com.pet.comes.dto.FamilyDto;
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
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public FamilyService(FamilyRepository familyRepository, Status status, ResponseMessage message){
        this.familyRepository = familyRepository; // initalized
        this.status = status;
        this.message = message;
    }

    public ResponseEntity addFamily(FamilyDto familyDto ){


        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }
}
