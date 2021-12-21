package com.pet.comes.controller;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.PinReqDto;
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

    @PutMapping("/{diaryId}")
    public ResponseEntity modifyDiary(@PathVariable Long diaryId,@RequestBody DiaryReqDto diaryReqDto ){
        return  diaryService.modifyDiary(diaryId,diaryReqDto);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity deleteDiary(@PathVariable Long diaryId){
        return diaryService.deleteDiary(diaryId);
    }

    @PutMapping("/toggle/{diaryId}")
    public ResponseEntity toggleDiary(@PathVariable Long diaryId){
        return diaryService.toggleDiary(diaryId);
    }

    @PostMapping("/pin")
    public ResponseEntity pinDiary(@RequestBody PinReqDto pinReqDto){
        return diaryService.pinDiary(pinReqDto);
    }

    @GetMapping("/list/pin/{userId}")
    public ResponseEntity pinList(@PathVariable Long userId){
        return diaryService.pinList(userId);
    }
}
