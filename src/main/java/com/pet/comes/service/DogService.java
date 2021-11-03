package com.pet.comes.service;

import com.pet.comes.repository.DogRepository;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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



}
