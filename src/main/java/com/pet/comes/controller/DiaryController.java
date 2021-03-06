package com.pet.comes.controller;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.DiaryUpdateReqDto;
import com.pet.comes.dto.Req.PinReqDto;
import com.pet.comes.model.EnumType.SearchType;
import com.pet.comes.model.EnumType.SortedType;
import com.pet.comes.service.DiaryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

//    @Autowired
//    public DiaryController(DiaryService diaryService) {
//        this.diaryService = diaryService;
//    }

    /* D1 : 강아지별 다이어리 조회 API (미완성) : 현재 공개/비공개 글 상관없이 전부 다 조회 -- Tony */
    @GetMapping("/{userId}/{nickName}/{dogName}")
    public ResponseEntity dogDiaryList(@PathVariable Long userId, @PathVariable String nickName, @PathVariable String dogName) {
        return diaryService.dogDiaryList(userId, nickName, dogName);
    }

    /* D2 다이어리 작성 API -- Tony */
    @PostMapping()
    public ResponseEntity writeDiary(@RequestBody DiaryReqDto diaryReqDto) {
        return diaryService.writeDiary(diaryReqDto);
    }

    /* D3 다이어리 수정 API -- Tony */
    @PatchMapping("/{diaryId}")
    public ResponseEntity modifyDiary(@PathVariable Long diaryId, @RequestBody DiaryUpdateReqDto diaryUpdateReqDto) {
        return diaryService.modifyDiary(diaryId, diaryUpdateReqDto);
    }

    /* D4 다이어리 삭제 API -- Tony */
    @DeleteMapping("/{diaryId}")
    public ResponseEntity deleteDiary(@PathVariable Long diaryId) {
        return diaryService.deleteDiary(diaryId);
    }

    /* D5 다이어리 공유 설정 변경 API -- Tony */
    @PutMapping("/toggle/{diaryId}")
    public ResponseEntity toggleDiary(@PathVariable Long diaryId) {
        return diaryService.toggleDiary(diaryId);
    }

    /* D7 다이어리 핀한수 상세보기 API -- Tony */
    @GetMapping("/pin/{diaryId}")
    public ResponseEntity getPinListofDiary(@PathVariable Long diaryId) {
        return diaryService.getPinListofDiary(diaryId);
    }

    /* S1 인기순, 최신순 다이어리 정렬, 조회 -- Tony */
    @GetMapping("/{sorted}")
    public ResponseEntity getAllDiary(@PathVariable SortedType sorted) {
        return diaryService.getAllDiary(sorted);
    }

    /* S2 공유다이어리 "내용, 작성자, 강아지" 이름으로 검색 --Tony */
    @GetMapping("/{option}/{text}")
    public ResponseEntity searchDiary(@PathVariable SearchType option, @PathVariable String text){
       return diaryService.searchDiary(option,text);
    }

    /* S9 다이어리 핀 설정하기/해제하기 API -- Tony */
    @PostMapping("/pin")
    public ResponseEntity pinDiary(@RequestBody PinReqDto pinReqDto) {
        return diaryService.pinDiary(pinReqDto);
    }


}
