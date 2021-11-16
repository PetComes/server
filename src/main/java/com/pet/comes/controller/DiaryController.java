package com.pet.comes.controller;


import com.pet.comes.repository.DiaryRepository;
import com.pet.comes.service.DiaryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }
}
