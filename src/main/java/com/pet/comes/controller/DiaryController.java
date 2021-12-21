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

    /* D1 다이어리 조회 API (미완성) : 완성시 diaryId 도 전달하기 -- Tony */
    @GetMapping("/{dogId}")
    public ResponseEntity dogDiaryList(@PathVariable Long dogId){
        return diaryService.dogDiaryList(dogId);
    }

    /* D2 다이어리 작성 API -- Tony */
    @PostMapping()
    public ResponseEntity writeDiary(@RequestBody DiaryReqDto diaryReqDto){
        return diaryService.writeDiary(diaryReqDto);
    }

    /* D3 다이어리 수정 API -- Tony */
    @PutMapping("/{diaryId}")
    public ResponseEntity modifyDiary(@PathVariable Long diaryId,@RequestBody DiaryReqDto diaryReqDto ){
        return  diaryService.modifyDiary(diaryId,diaryReqDto);
    }

    /* D4 다이어리 삭제 API -- Tony */
    @DeleteMapping("/{diaryId}")
    public ResponseEntity deleteDiary(@PathVariable Long diaryId){
        return diaryService.deleteDiary(diaryId);
    }

    /* D5 다이어리 공유 설정 변경 API -- Tony */
    @PutMapping("/toggle/{diaryId}")
    public ResponseEntity toggleDiary(@PathVariable Long diaryId){
        return diaryService.toggleDiary(diaryId);
    }

    /* D7 다이어리 핀한수 상세보기 API -- Tony */
    @GetMapping("/pin/{diaryId}")
    public ResponseEntity getPinListofDiary(@PathVariable Long diaryId){
        return diaryService.getPinListofDiary(diaryId);
    }

    /* S9 다이어리 핀 설정하기/해제하기 API -- Tony */
    @PostMapping("/pin")
    public ResponseEntity pinDiary(@RequestBody PinReqDto pinReqDto){
        return diaryService.pinDiary(pinReqDto);
    }

    /* H6 내 핀 목록 조회 API -- Tony */
    @GetMapping("/list/pin/{userId}")
    public ResponseEntity pinList(@PathVariable Long userId){
        return diaryService.pinList(userId);
    }
}
