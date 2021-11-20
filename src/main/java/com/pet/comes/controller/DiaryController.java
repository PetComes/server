package com.pet.comes.controller;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService){
        this.diaryService = diaryService;
    }

    @PostMapping()
    public ResponseEntity writeDiary(@RequestBody DiaryReqDto diaryReqDto){
        return diaryService.writeDiary(diaryReqDto);
    }

    @GetMapping("/{dogId}")
    public ResponseEntity dogDiaryList(@PathVariable Long dogId){
        return diaryService.dogDiaryList(dogId);
    }

}
