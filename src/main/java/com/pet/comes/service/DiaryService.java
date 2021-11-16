package com.pet.comes.service;


import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final Status status;
    private final ResponseMessage message;

    @Autowired
    public DiaryService(DiaryRepository diaryRepository, Status status, ResponseMessage message){
        this.diaryRepository =diaryRepository;
        this.status = status;
        this.message = message;
    }
}
